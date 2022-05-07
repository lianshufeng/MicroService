package com.github.microservice.auth.server.core.service.auth;

import com.github.microservice.auth.client.type.DeviceType;
import com.github.microservice.auth.client.type.GrantType;
import com.github.microservice.auth.security.util.TimeUtil;
import com.github.microservice.auth.server.core.auth.endpoint.AuthHelper;
import com.github.microservice.auth.server.core.auth.endpoint.AuthProcess;
import com.github.microservice.auth.server.core.dao.UserDao;
import com.github.microservice.auth.server.core.exception.UserDisableException;
import com.github.microservice.auth.server.core.exception.UserNotExistException;
import com.github.microservice.auth.server.core.service.user.UserManager;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, Serializable {

    public static final String TimeText = "time:";

    private static final long serialVersionUID = 1L;

    @Autowired
    private UserManager userManager;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private UserDao userDao;


    /**
     * 构建权限的用户名
     *
     * @param deviceType
     * @param uid
     * @return
     */
    public static String buildOAuthUserName(DeviceType deviceType, String uid) {
        return deviceType.name() + ":" + uid;
    }


    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String loginValue) throws UsernameNotFoundException {
        final AuthProcess authProcess = authHelper.getAuthProcess();
        com.github.microservice.auth.server.core.domain.User user = null;
        //生成令牌
        if (authProcess.getGrantType() == GrantType.password) {
            user = userManager.get(authProcess.getLoginType()).getUser(loginValue);
            Assert.state(user != null, "账号不存在");
        } else if (authProcess.getGrantType() == GrantType.refresh_token) {
            //刷新令牌使用uid查询
            user = this.userDao.findTop1ById(loginValue.substring(loginValue.lastIndexOf(":") + 1));
        }

        if (user == null) {
            throw new UserNotExistException();
        }

        //是否禁用
        if (user.isDisable()) {
            throw new UserDisableException();
        }

        //设置内存对象的用户id
        authProcess.setUid(user.getId());
        //device + uid
        return new User(
                buildOAuthUserName(authProcess.getDeviceType(), user.getId()),
                user.getPassWord(),
                List.of(() -> {
                    return "user";
                }, () -> {
                    return TimeText + TimeUtil.getTime();
                }));
    }
}
