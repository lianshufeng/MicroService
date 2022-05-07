package com.github.microservice.auth.server.core.auth.endpoint;

import com.github.microservice.auth.client.type.DeviceType;
import com.github.microservice.auth.client.type.GrantType;
import com.github.microservice.auth.client.type.LoginType;
import com.github.microservice.auth.server.core.conf.AuthConf;
import com.github.microservice.auth.server.core.dao.UserLoginLogDao;
import com.github.microservice.auth.server.core.dao.UserTokenDao;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.auth.server.core.domain.UserLoginLog;
import com.github.microservice.auth.server.core.domain.UserToken;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
public class AuthHelper {

    @Autowired
    private AuthConf authConf;

    @Autowired
    private UserTokenDao userTokenDao;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private UserLoginLogDao userLoginLogDao;

    @Autowired
    private HttpServletRequest request;

    //线程容器
    private ThreadLocal<AuthProcess> threadLocal = new ThreadLocal();


    /**
     * 放入当前线程
     *
     * @param parameters
     */
    public void put(Map<String, String> parameters) {
        AuthProcess authProcess = AuthProcess
                .builder()
                .grantType(GrantType.valueOf(parameters.get("grant_type")))
                .parameters(parameters)
                .loginName(parameters.get("username"))
                .passWord(parameters.get("password"))
                .loginType(LoginType.valueOf(parameters.getOrDefault("loginType", authConf.getDefaultLoginType().name())))
                .deviceType(DeviceType.valueOf(parameters.getOrDefault("deviceType", authConf.getDefaultDeviceType().name())))
                .deviceUUid(parameters.get("deviceUUid"))
                .deviceIp(parameters.get("deviceIp"))
                .deviceUserAgent(parameters.get("deviceUserAgent"))
                .clientId(parameters.get("client_id"))
                .accessTokenTimeOut(Integer.parseInt(parameters.getOrDefault("accessTokenTimeOut", String.valueOf(authConf.getAccessTokenValiditySeconds()))))
                .refreshTokenTimeOut(Integer.parseInt(parameters.getOrDefault("refreshTokenTimeOut", String.valueOf(authConf.getRefreshTokenValiditySeconds()))))
                .build();
        this.threadLocal.set(authProcess);
    }


    /**
     * 取出校验密码
     *
     * @return
     */
    public AuthProcess getAuthProcess() {
        return this.threadLocal.get();
    }


    /**
     * 释放
     */
    public void release(OAuth2AccessToken oAuth2AccessToken) {
        if (oAuth2AccessToken == null) {
            return;
        }

        //记录日志
        try {
            Optional.ofNullable(getAuthProcess()).ifPresent((authProcess) -> {
                UserLoginLog userLoginLog = new UserLoginLog();
                BeanUtils.copyProperties(authProcess, userLoginLog);
                Optional.ofNullable(authProcess.getUid()).ifPresent((uid) -> {
                    userLoginLog.setUser(User.build(uid));
                });
                userLoginLog.setAccessToken(oAuth2AccessToken.getValue());
                Optional.ofNullable(oAuth2AccessToken.getRefreshToken()).ifPresent((refreshToken) -> {
                    userLoginLog.setRefreshToken(refreshToken.getValue());
                });
                this.userLoginLogDao.save(userLoginLog);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        //记录令牌
        try {
            this.saveToDb(oAuth2AccessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //移出环境
        this.threadLocal.remove();
    }


    /**
     * 保存到DB中
     *
     * @param oAuth2AccessToken
     */
    private void saveToDb(OAuth2AccessToken oAuth2AccessToken) {
        //取出令牌,并存入到mongo中方便业务查询
        final String refreshToken = oAuth2AccessToken.getRefreshToken().getValue();
        if (StringUtils.hasText(refreshToken) && !this.userTokenDao.existsByRefreshToken(refreshToken)) {
            AuthProcess authProcess = getAuthProcess();
            UserToken userToken = new UserToken();
            userToken.setUser(User.build(authProcess.getUid()));
            userToken.setDeviceType(authProcess.getDeviceType());
            userToken.setDeviceUUid(authProcess.getDeviceUUid());
            userToken.setDeviceIp(authProcess.getDeviceIp());
            userToken.setDeviceUserAgent(authProcess.getDeviceUserAgent());
            userToken.setRefreshToken(refreshToken);
            userToken.setClientId(authProcess.getClientId());
            userToken.setTTL(new Date(this.dbHelper.getTime() + authProcess.getRefreshTokenTimeOut() * 1000L));
            this.dbHelper.saveTime(userToken);
            this.userTokenDao.save(userToken);
        }
    }


}
