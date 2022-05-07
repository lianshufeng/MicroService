package com.github.microservice.auth.server.core.service.user.impl;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.content.ResultState;
import com.github.microservice.auth.client.model.UserAuthModel;
import com.github.microservice.auth.client.type.LoginType;
import com.github.microservice.auth.server.core.dao.TokenLoginDao;
import com.github.microservice.auth.server.core.dao.UserDao;
import com.github.microservice.auth.server.core.domain.TokenLogin;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.auth.server.core.service.user.LocalUserService;
import com.github.microservice.auth.server.core.service.user.mode.LocalUserTokenLoginModel;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.github.microservice.core.util.token.TokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LocalUserServiceFromToken implements LocalUserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private TokenLoginDao tokenLoginDao;

    @Override
    public LoginType type() {
        return LoginType.Token;
    }

    @Override
    public ResultContent<String> add(UserAuthModel userAuth) {
        if (!(userAuth instanceof LocalUserTokenLoginModel)) {
            return ResultContent.build(ResultState.TokenLoginParmError);
        }
        final LocalUserTokenLoginModel userTokenLoginModel = (LocalUserTokenLoginModel) userAuth;

        final String token = TokenUtil.create();

        TokenLogin tokenLogin = new TokenLogin();
        tokenLogin.setUser(User.build(userTokenLoginModel.getUid()));
        tokenLogin.setToken(token);
        tokenLogin.setValue(this.passwordEncoder.encode(userTokenLoginModel.getPassWord()));

        tokenLogin.setTTL(new Date(this.dbHelper.getTime() + userTokenLoginModel.getTimeOut() * 1000L));
        tokenLogin.setCurrentCheckCount(0);
        tokenLogin.setMaxCheckCount(userTokenLoginModel.getMaxCheckCount());
        this.dbHelper.saveTime(tokenLogin);
        this.tokenLoginDao.save(tokenLogin);

        return ResultContent.build(ResultState.Success, token);
    }

    @Override
    public User getUser(String loginValue) {
        TokenLogin tokenLogin = this.tokenLoginDao.findByToken(loginValue);
        if (tokenLogin == null) {
            return null;
        }
        return toUser(tokenLogin);
    }

    @Override
    public User checkAndGet(UserAuthModel userAuth) {

        final TokenLogin tokenLogin = this.tokenLoginDao.findByToken(userAuth.getLoginValue());
        if (tokenLogin == null) {
            return null;
        }

        //校验检查次数
        if (tokenLogin.getCurrentCheckCount() >= tokenLogin.getMaxCheckCount()) {
            return null;
        }
        this.tokenLoginDao.incCheckCount(userAuth.getLoginValue());


        final User user = toUser(tokenLogin);
        if (user == null) {
            return null;
        }

        //校验密码
        return passwordEncoder.matches(userAuth.getPassWord(), user.getPassWord()) ? user : null;

    }


    /**
     * 转换到用户
     *
     * @param tokenLogin
     * @return
     */
    private User toUser(TokenLogin tokenLogin) {
        User dbUser = tokenLogin.getUser();
        User user = new User();
        BeanUtils.copyProperties(dbUser, user);

        //密码为令牌的密码
        user.setPassWord(tokenLogin.getValue());
        return user;
    }

}
