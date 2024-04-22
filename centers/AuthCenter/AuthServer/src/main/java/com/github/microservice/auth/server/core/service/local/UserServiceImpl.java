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
import com.github.microservice.auth.security.model.UserAuthenticationToken;
import com.github.microservice.auth.server.core.auth.endpoint.AuthHelper;
import com.github.microservice.auth.server.core.auth.endpoint.AuthProcess;
import com.github.microservice.auth.server.core.conf.AuthConf;
import com.github.microservice.auth.server.core.dao.TokenLoginDao;
import com.github.microservice.auth.server.core.dao.UserDao;
import com.github.microservice.auth.server.core.dao.UserLoginLogDao;
import com.github.microservice.auth.server.core.dao.UserTokenDao;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.auth.server.core.domain.UserLoginLog;
import com.github.microservice.auth.server.core.domain.UserToken;
import com.github.microservice.auth.server.core.helper.TokenHelper;
import com.github.microservice.auth.server.core.oauth2.authentication.OAuth2Authentication;
import com.github.microservice.auth.server.core.oauth2.request.OAuth2Request;
import com.github.microservice.auth.server.core.oauth2.service.AuthorizationServerTokenServices;
import com.github.microservice.auth.server.core.oauth2.store.TokenStore;
import com.github.microservice.auth.server.core.oauth2.token.DefaultOAuth2AccessToken;
import com.github.microservice.auth.server.core.oauth2.token.DefaultOAuth2RefreshToken;
import com.github.microservice.auth.server.core.oauth2.token.OAuth2AccessToken;
import com.github.microservice.auth.server.core.oauth2.token.OAuth2RefreshToken;
import com.github.microservice.auth.server.core.service.auth.UserDetailsServiceImpl;
import com.github.microservice.auth.server.core.service.user.UserManager;
import com.github.microservice.auth.server.core.service.user.mode.LocalUserTokenLoginModel;
import com.github.microservice.auth.server.core.util.JWTUtil;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.github.microservice.core.util.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@Primary
public class UserServiceImpl implements UserService {

    public static final AuthStreamType StreamType = AuthStreamType.User;

    @Value("${server.port}")
    private int port;


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
    private UserLoginLogDao userLoginLogDao;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private TokenHelper tokenHelper;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


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
    @Transactional
    public ResultContent<LoginTokenModel> login(UserAuthLoginModel loginModel) {
        validateUserAuth(loginModel);

        // 验证用户账号和密码是否正确
        com.github.microservice.auth.server.core.domain.User user = this.userManager.get(loginModel.getLoginType()).checkAndGet(loginModel);
        if (user == null) {
            return ResultContent.build(ResultState.UserPasswordError);
        }
        // 用户禁用
        if (user.isDisable()) {
            return ResultContent.build(ResultState.UserDisable);
        }


        //登陆则注销之前登陆的所有令牌,单一设备只能允许登陆一次
        if (authConf.isOnlyOneDeviceLogin()) {
            Optional.ofNullable(this.userTokenDao.findByClientIdAndDeviceTypeAndUser(loginModel.getClientId(), loginModel.getDeviceType(), User.build(user.getId()))).ifPresent(it -> {
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


        //转换为 UserDetails模型
        AuthProcess authProcess = new AuthProcess();
        BeanUtils.copyProperties(loginModel, authProcess);
        authProcess.setGrantType(GrantType.password);
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(loginModel.getLoginValue(), authProcess);


        //转发到oAuth2.0 进行登陆
        LoginTokenModel token = oAuthToken(loginModel, userDetails, user);
        if (token != null && StringUtils.hasText(token.getAccess_token())) {
            this.tokenEventStreamHelper.publish(AuthStreamType.Token, new TokenStreamModel(AuthEventType.Add, token.getAccess_token(), new Object[]{user.getId()}));
        }
        return ResultContent.buildContent(token);
    }

    @Override
    public ResultContent<UserModel> queryFromLoginType(LoginType loginType, String loginValue) {
        User user = this.userManager.get(loginType).getUser(loginValue);
        if (user == null) {
            return ResultContent.build(ResultState.UserNotExists);
        }
        return ResultContent.buildContent(toModel(user));
    }

    @Override
    public ResultContent<Void> updateLoginPassword(String uid, String passWord) {
        User user = this.userDao.findTop1ById(uid);
        if (user == null) {
            return ResultContent.build(ResultState.UserNotExists);
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
            return ResultContent.build(ResultState.UserNotExists);
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
        return ResultContent.buildContent(oAuth2AccessTokenToUserTokenModel(oAuth2AccessToken));
    }

    @Override
    @SneakyThrows
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


        //查询对应的访问令牌,如果访问令牌存在则删除
        final OAuth2AccessToken accessToken = this.tokenStore.getAccessToken(oAuth2Authentication);
        if (accessToken != null) {
            this.tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
        }


        //取出存入信息
        final JWTUtil.JwtModel jwtModel = this.tokenHelper.parser(oAuth2RefreshToken.getValue());
        final String uid = jwtModel.getSubject();


        //转换为登录模型
        final UserAuthLoginModel loginModel = JsonUtil.toObject(JsonUtil.toJson(jwtModel.getClaims()), UserAuthLoginModel.class);

        //创建访问令牌
        DefaultOAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(this.tokenHelper.create(uid, jwtModel.getClaims(), loginModel.getAccessTokenTimeOut() * 1000));
        oAuth2AccessToken.setRefreshToken(oAuth2RefreshToken);
        oAuth2AccessToken.setExpiration(new Date(dbHelper.getTime() + loginModel.getAccessTokenTimeOut() * 1000));
        tokenStore.storeAccessToken(oAuth2AccessToken, oAuth2Authentication);


        //转换为用户令牌模型
        final UserTokenModel userTokenModel = oAuth2AccessTokenToUserTokenModel(oAuth2AccessToken);

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

    @Override
    public ResultContent<Void> unRegister(String uid, String passWord) {
        User user = this.userDao.findTop1ById(uid);
        if (user == null) {
            return ResultContent.build(ResultState.UserNotExists);
        }
        if (!passwordEncoder.matches(passWord, user.getPassWord())) {
            return ResultContent.build(ResultState.UserPasswordError);
        }
        this.logoutFromUid(null, uid);

        boolean success = this.userDao.unRegister(uid);
        if (success) {
            this.authEventStreamHelper.publish(new UserStreamModel(AuthEventType.UnRegister, uid));
        }
        return ResultContent.build(success);
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
    private UserTokenModel oAuth2AccessTokenToUserTokenModel(OAuth2AccessToken oAuth2AccessToken) {
        if (oAuth2AccessToken == null) {
            return null;
        }

        //jwt
        String uid = this.tokenHelper.parser(oAuth2AccessToken.getValue()).getSubject();


        UserTokenModel userTokenModel = new UserTokenModel();
        //访问令牌
        userTokenModel.setAccessToken(oAuth2AccessToken.getValue());
        //到期时间
//        userTokenModel.setExpireTime(time + oAuth2AccessToken.getExpiresIn() * 1000);
        userTokenModel.setExpireTime(((Integer) oAuth2AccessToken.getExpiresIn()).longValue());
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
    public LoginTokenModel oAuthToken(UserAuthLoginModel loginModel, UserDetails userDetails, User user) {
        OAuth2Request storedRequest = new OAuth2Request(loginModel.getClientId());


        UserAuthenticationToken userAuthenticationToken = new UserAuthenticationToken(null);
        userAuthenticationToken.setPrincipal(userDetails);

        Authentication userAuthentication = new OAuth2Authentication(storedRequest, userAuthenticationToken);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(storedRequest, userAuthentication);


        //登录信息
        final Map<String, Object> claims = BeanMap.create(loginModel);

        //刷新令牌
        DefaultOAuth2RefreshToken oAuth2RefreshToken = new DefaultOAuth2RefreshToken(this.tokenHelper.create(user.getId(), claims, loginModel.getRefreshTokenTimeOut() * 1000));


        //访问令牌
        DefaultOAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(this.tokenHelper.create(user.getId(), claims, loginModel.getAccessTokenTimeOut() * 1000));
        oAuth2AccessToken.setRefreshToken(oAuth2RefreshToken);
        oAuth2AccessToken.setExpiration(new Date(dbHelper.getTime() + loginModel.getAccessTokenTimeOut() * 1000));


        // 保存 redis
        tokenStore.storeAccessToken(oAuth2AccessToken, oAuth2Authentication);
        tokenStore.storeRefreshToken(oAuth2RefreshToken, oAuth2Authentication);

        //记录token
        this.storeUserToken(loginModel, oAuth2RefreshToken, oAuth2AccessToken);


        LoginTokenModel loginTokenModel = new LoginTokenModel();
        loginTokenModel.setAccess_token(oAuth2AccessToken.getValue());
        loginTokenModel.setRefresh_token(oAuth2RefreshToken.getValue());
        loginTokenModel.setToken_type(oAuth2AccessToken.getTokenType());
        loginTokenModel.setScope(null);
        loginTokenModel.setExpires_in(oAuth2AccessToken.getExpiresIn());
        return loginTokenModel;
    }

    /**
     * 记录用户令牌
     * @param loginModel
     * @param oAuth2RefreshToken
     * @param oAuth2AccessToken
     */
    private void storeUserToken(UserAuthLoginModel loginModel, DefaultOAuth2RefreshToken oAuth2RefreshToken, DefaultOAuth2AccessToken oAuth2AccessToken) {
        final String refreshToken = oAuth2RefreshToken.getValue();
        if (StringUtils.hasText(refreshToken) && !this.userTokenDao.existsByRefreshToken(refreshToken)) {
            JWTUtil.JwtModel jwtModel = tokenHelper.parser(oAuth2RefreshToken.getValue());
            final User user = User.build(jwtModel.getSubject());

            // 用户令牌
            UserToken userToken = new UserToken();
            userToken.setUser(user);
            userToken.setDeviceType(loginModel.getDeviceType());
            userToken.setDeviceUUid(loginModel.getDeviceUUid());
            userToken.setDeviceIp(loginModel.getDeviceIp());
            userToken.setDeviceUserAgent(loginModel.getDeviceUserAgent());
            userToken.setRefreshToken(refreshToken);
            userToken.setClientId(loginModel.getClientId());
            userToken.setTTL(jwtModel.getExpiration());
            this.dbHelper.saveTime(userToken);
            this.userTokenDao.insert(userToken);


            //登录记录
            UserLoginLog userLoginLog = new UserLoginLog();
            BeanUtils.copyProperties(loginModel, userLoginLog);
            userLoginLog.setUser(user);
            userLoginLog.setAccessToken(oAuth2AccessToken.getValue());
            userLoginLog.setRefreshToken(oAuth2RefreshToken.getValue());
            this.dbHelper.saveTime(userLoginLog);
            this.userLoginLogDao.insert(userLoginLog);
        }
    }


}

