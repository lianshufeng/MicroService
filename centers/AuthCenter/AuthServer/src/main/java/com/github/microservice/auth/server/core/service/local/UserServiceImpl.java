package com.github.microservice.auth.server.core.service.local;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.content.ResultState;
import com.github.microservice.auth.client.helper.AuthEventStreamHelper;
import com.github.microservice.auth.client.helper.TokenEventStreamHelper;
import com.github.microservice.auth.client.model.*;
import com.github.microservice.auth.client.model.stream.TokenStreamModel;
import com.github.microservice.auth.client.model.stream.UserStreamModel;
import com.github.microservice.auth.client.service.UserService;
import com.github.microservice.auth.client.type.*;
import com.github.microservice.auth.security.util.TimeUtil;
import com.github.microservice.auth.server.core.auth.endpoint.AuthHelper;
import com.github.microservice.auth.server.core.conf.AuthConf;
import com.github.microservice.auth.server.core.dao.TokenLoginDao;
import com.github.microservice.auth.server.core.dao.UserDao;
import com.github.microservice.auth.server.core.dao.UserTokenDao;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.auth.server.core.service.auth.UserDetailsServiceImpl;
import com.github.microservice.auth.server.core.service.user.UserManager;
import com.github.microservice.auth.server.core.service.user.mode.LocalUserTokenLoginModel;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.github.microservice.core.util.bean.BeanUtil;
import com.github.microservice.core.util.result.InvokerResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    public static final AuthStreamType StreamType = AuthStreamType.User;

    @Value("${server.port}")
    private int port;

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthorizationServerTokenServices tokenServices;

    @Autowired
    private TokenStore tokenStore;


    @Autowired
    private AuthConf authConf;


    @Autowired
    private UserTokenDao userTokenDao;


    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private AuthEventStreamHelper authEventStreamHelper;

    @Autowired
    private TokenEventStreamHelper tokenEventStreamHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenLoginDao tokenLoginDao;

    @Autowired
    private DBHelper dbHelper;


    @Override
    public ResultContent<String> add(UserAuthModel user) {
        validateUserAuth(user);
        ResultContent<String> resultContent = userManager.get(user.getLoginType()).add(user);
        if (resultContent.getState() == ResultState.Success) {
            this.authEventStreamHelper.publish(new UserStreamModel(AuthEventType.Add, resultContent.getContent()));
        }
        return resultContent;
    }

    @Override
    public ResultContent<UserModel> get(String uid) {
        return ResultContent.buildContent(toModel(this.userDao.findTop1ById(uid)));
    }

    @Override
    public ResultContent<Void> updateLoginName(UserLoginModel userLogin) {
        Assert.hasText(userLogin.getId(), "用户id不能为空");
        Assert.hasText(userLogin.getLoginValue(), "登陆名不能为空");
        Assert.notNull(userLogin.getLoginType(), "登陆类型不能为空");
        boolean success = this.userDao.updateLoginName(userLogin);
        if (success) {
            this.authEventStreamHelper.publish(new UserStreamModel(AuthEventType.Modify, userLogin.getId()));
        }
        return ResultContent.build(success);
    }

    @Override
    public ResultContent<String> addTokenLogin(TokenLoginModel tokenLoginModel) {
        if (!this.userDao.existsById(tokenLoginModel.getUid())) {
            return ResultContent.build(ResultState.UserNotExists);
        }
        LocalUserTokenLoginModel userTokenLogin = new LocalUserTokenLoginModel();
        BeanUtils.copyProperties(tokenLoginModel, userTokenLogin);

        userTokenLogin.setPassWord(tokenLoginModel.getValue());
        userTokenLogin.setLoginType(LoginType.Token);

        return this.userManager.get(LoginType.Token).add(userTokenLogin);
    }

    @Override
    public ResultContent<Void> removeTokenLogin(String token) {
        return ResultContent.build(this.tokenLoginDao.removeByToken(token) > 0);
    }

    @Override
    @SneakyThrows
    public ResultContent<LoginTokenModel> login(UserAuthLoginModel user) {
        validateUserAuth(user);

        // 验证用户账号和密码是否正确
        com.github.microservice.auth.server.core.domain.User localUser = this.userManager.get(user.getLoginType()).checkAndGet(user);
        if (localUser == null) {
            return ResultContent.build(ResultState.UserPasswordError);
        }
        // 用户禁用
        if (localUser.isDisable()) {
            return ResultContent.build(ResultState.UserDisable);
        }


        //登陆则注销之前登陆的所有令牌,单一设备只能允许登陆一次
        if (authConf.isOnlyOneDeviceLogin()) {
            Optional.ofNullable(this.userTokenDao.findByClientIdAndDeviceTypeAndUser(user.getClientId(), user.getDeviceType(), User.build(localUser.getId()))).ifPresent(it -> {
                it.forEach((userToken) -> {
                    //标记本条数据过期，等待定时器删除
                    this.userTokenDao.setTTL(userToken.getRefreshToken(), 0l);
                    Optional.ofNullable(this.tokenStore.readRefreshToken(userToken.getRefreshToken())).ifPresent((oAuth2RefreshToken) -> {
                        this.tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
                        this.tokenStore.removeRefreshToken(oAuth2RefreshToken);
                    });
                });
            });

        }

        //转发到oAuth2.0 进行登陆
        LoginTokenModel token = oAuthToken(user);
        if (token != null && StringUtils.hasText(token.getAccess_token())) {
            this.tokenEventStreamHelper.publish(AuthStreamType.Token, new TokenStreamModel(AuthEventType.Add, token.getAccess_token(), new Object[]{localUser.getId()}));
        }
        return ResultContent.buildContent(token);
    }

    @Override
    public ResultContent<UserModel> queryFromLoginType(LoginType loginType, String loginValue) {
        User user = this.userManager.get(loginType).getUser(loginValue);
        if (user == null) {
            return ResultContent.build(ResultState.UserExists);
        }
        return ResultContent.buildContent(toModel(user));
    }

    @Override
    public ResultContent<Void> updateLoginPassword(String uid, String passWord) {
        User user = this.userDao.findTop1ById(uid);
        if (user == null) {
            return ResultContent.buildContent(ResultState.UserExists);
        }
        boolean success = this.userDao.updatePassword(uid, passwordEncoder.encode(passWord));
        if (success) {
            this.authEventStreamHelper.publish(new UserStreamModel(AuthEventType.Modify, uid));
        }
        return ResultContent.build(success);
    }

    @Override
    public ResultContent<Void> checkLoginPassword(String uid, String passWord) {
        User user = this.userDao.findTop1ById(uid);
        if (user == null) {
            return ResultContent.buildContent(ResultState.UserExists);
        }
        return ResultContent.build(passwordEncoder.matches(passWord, user.getPassWord()) ? ResultState.Success : ResultState.UserPasswordError);
    }

    @Override
    public ResultContent<UserTokenModel> queryToken(String accessToken) {
        Assert.hasText(accessToken, "访问令牌不能为空");
        final OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(accessToken);
        if (oAuth2AccessToken == null) {
            return ResultContent.build(ResultState.AccessTokenError);
        }
        //查询令牌详情
        final OAuth2Authentication oAuth2Authentication = this.tokenStore.readAuthentication(oAuth2AccessToken.getValue());

        //令牌创建时间
        Long createTime = null;
        for (GrantedAuthority authority : oAuth2Authentication.getAuthorities()) {
            String auth = authority.getAuthority();
            if (auth.indexOf(UserDetailsServiceImpl.TimeText) == 0) {
                createTime = Long.parseLong(auth.substring(UserDetailsServiceImpl.TimeText.length()));
                break;
            }
        }

        // uid
        final String uid = oAuth2Authentication.getName().substring(oAuth2Authentication.getName().indexOf(":") + 1);

        return ResultContent.buildContent(oAuth2AccessTokenToUserTokenModel(createTime, uid, oAuth2AccessToken));
    }

    @Override
    public ResultContent<UserTokenModel> refreshToken(final String refreshToken) {
        Assert.hasText(refreshToken, "刷新令牌不能为空");
        final OAuth2RefreshToken oAuth2RefreshToken = this.tokenStore.readRefreshToken(refreshToken);
        if (oAuth2RefreshToken == null) {
            return ResultContent.build(ResultState.RefreshTokenError);
        }
        final OAuth2Authentication oAuth2Authentication = this.tokenStore.readAuthenticationForRefreshToken(oAuth2RefreshToken);
        if (oAuth2Authentication == null) {
            return ResultContent.build(ResultState.RefreshTokenError);
        }

        //构建token的生成参数
        final OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();

        //刷新token的参数
        final Map<String, String> requestParameters = Map.of(
                "grant_type", GrantType.refresh_token.name(),
                "client_id", oAuth2Request.getRequestParameters().get("client_id"),
                "refresh_token", refreshToken
        );

        //设置请求token的参数
        this.authHelper.put(requestParameters);
        OAuth2AccessToken newToken = null;
        try {
            final TokenRequest tokenRequest = new TokenRequest(
                    requestParameters,
                    oAuth2Request.getClientId(),
                    oAuth2Request.getScope(),
                    oAuth2Request.getGrantType()
            );
            newToken = this.tokenServices.refreshAccessToken(refreshToken, tokenRequest);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.authHelper.release(newToken);
        }
        //用户id
        final String uid = oAuth2Authentication.getName().substring(oAuth2Authentication.getName().indexOf(":") + 1);

        //转换为用户令牌模型
        final UserTokenModel userTokenModel = oAuth2AccessTokenToUserTokenModel(TimeUtil.getTime(), uid, newToken);

        if (userTokenModel != null && StringUtils.hasText(userTokenModel.getAccessToken())) {
            this.tokenEventStreamHelper.publish(AuthStreamType.Token, new TokenStreamModel(AuthEventType.Add, userTokenModel.getAccessToken(), new Object[]{uid}));
        }
        return ResultContent.buildContent(userTokenModel);
    }

    @Override
    public ResultContent<Long> logoutFromUid(DeviceType deviceType, String uid) {
        AtomicLong atomicLong = new AtomicLong();
        Optional.ofNullable(this.userTokenDao.findUserToken(deviceType, uid)).ifPresent((userTokens) -> {
            userTokens.stream().map((token) -> {
                return this.tokenStore.readRefreshToken(token.getRefreshToken());
            }).forEach((oAuth2RefreshToken) -> {
                this.tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
                this.tokenStore.removeRefreshToken(oAuth2RefreshToken);
                atomicLong.addAndGet(1);
            });
        });
        long count = atomicLong.get();
        return ResultContent.build(count > 0 ? ResultState.Success : ResultState.Fail, count);
    }

    @Override
    public ResultContent<Long> logoutFromToken(String accessToken) {
        //取出登陆令牌
        OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(accessToken);
        if (oAuth2AccessToken == null) {
            return ResultContent.build(ResultState.AccessTokenError);
        }

        //刷新令牌
        OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
        this.tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
        this.tokenStore.removeRefreshToken(oAuth2RefreshToken);
        return ResultContent.build(ResultState.Success, 1);
    }

    @Override
    public ResultContent<Void> disable(String uid) {
        if (!this.userDao.disable(uid, true)) {
            return ResultContent.build(ResultState.Fail);
        }
        //注销所有设备,禁用后调用注销方法,token失效后会自动删除
        this.logoutFromUid(null, uid);
        this.authEventStreamHelper.publish(new UserStreamModel(AuthEventType.Disable, uid));
        return ResultContent.build(ResultState.Success);
    }

    @Override
    public ResultContent<Void> enable(String uid) {
        if (!this.userDao.disable(uid, false)) {
            return ResultContent.build(ResultState.Fail);
        }
        this.authEventStreamHelper.publish(new UserStreamModel(AuthEventType.Enable, uid));
        return ResultContent.build(ResultState.Success);
    }

    /**
     * 用户数据库模型转换为model
     *
     * @param user
     * @return
     */
    public static UserModel toModel(User user) {
        if (user == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user, userModel);
        userModel.setUserId(user.getId());
        return userModel;
    }


    /**
     * 模型转换
     *
     * @param oAuth2AccessToken
     * @return
     */
    private UserTokenModel oAuth2AccessTokenToUserTokenModel(long time, String uid, OAuth2AccessToken oAuth2AccessToken) {
        if (oAuth2AccessToken == null) {
            return null;
        }

        UserTokenModel userTokenModel = new UserTokenModel();
        //访问令牌
        userTokenModel.setAccessToken(oAuth2AccessToken.getValue());
        //到期时间
        userTokenModel.setExpireTime(time + oAuth2AccessToken.getExpiresIn() * 1000);
        //用户id
        userTokenModel.setUid(uid);
        return userTokenModel;
    }


    /**
     * 校验用户权限
     *
     * @param user
     */
    private void validateUserAuth(UserAuthModel user) {
        Assert.isTrue(user != null, "用户参数不能为空");
        Assert.notNull(user.getLoginType(), "登陆类型不能为空");
        Assert.hasText(user.getLoginValue(), "登陆名不能为空");
        Assert.hasText(user.getPassWord(), "密码不能为空");
    }


    /**
     * oAuth进行本地网络登陆
     *
     * @param user
     */
    @SneakyThrows
    private LoginTokenModel oAuthToken(UserAuthLoginModel user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        //转换为用户登陆模型
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        map.put("username", List.of(user.getLoginValue()));
        map.put("password", List.of(user.getPassWord()));
        map.put("grant_type", List.of("password"));
        map.put("client_id", List.of(user.getClientId()));
        map.put("client_secret", List.of(user.getClientSecret()));

        Optional.ofNullable(user.getDeviceType()).ifPresent((it) -> {
            map.put("deviceType", List.of(it.name()));
        });
        Optional.ofNullable(user.getDeviceUUid()).ifPresent((it) -> {
            map.put("deviceUUid", List.of(it));
        });
        Optional.ofNullable(user.getDeviceIp()).ifPresent((it) -> {
            map.put("deviceIp", List.of(it));
        });
        Optional.ofNullable(user.getDeviceUserAgent()).ifPresent((it) -> {
            map.put("deviceUserAgent", List.of(it));
        });
        Optional.ofNullable(user.getLoginType()).ifPresent((it) -> {
            map.put("loginType", List.of(it.name()));
        });

        Optional.ofNullable(user.getAccessTokenTimeOut()).ifPresent((it) -> {
            map.put("accessTokenTimeOut", List.of(String.valueOf(it)));
        });

        Optional.ofNullable(user.getRefreshTokenTimeOut()).ifPresent((it) -> {
            map.put("refreshTokenTimeOut", List.of(String.valueOf(it)));
        });


        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<InvokerResult> response = restTemplate.postForEntity("http://127.0.0.1:" + port + "/oauth/token", request, InvokerResult.class);

        LoginTokenModel loginTokenModel = new LoginTokenModel();
        BeanUtil.setBean(loginTokenModel, (Map) response.getBody().getContent());
        return loginTokenModel;
    }

}
