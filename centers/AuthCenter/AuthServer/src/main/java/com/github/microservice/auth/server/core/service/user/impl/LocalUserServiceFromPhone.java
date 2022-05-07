package com.github.microservice.auth.server.core.service.user.impl;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.content.ResultState;
import com.github.microservice.auth.client.model.UserAuthModel;
import com.github.microservice.auth.client.type.LoginType;
import com.github.microservice.auth.server.core.dao.UserDao;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.auth.server.core.service.user.LocalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LocalUserServiceFromPhone implements LocalUserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginType type() {
        return LoginType.Phone;
    }

    @Override
    public ResultContent<String> add(UserAuthModel userAuth) {
        if (userDao.existsByPhone(userAuth.getLoginValue())) {
            return ResultContent.build(ResultState.UserExists);
        }
        User user = new User();
        user.setPhone(userAuth.getLoginValue());
        user.setPassWord(this.passwordEncoder.encode(userAuth.getPassWord()));
        this.userDao.save(user);
        return ResultContent.build(ResultState.Success, user.getId());
    }

    @Override
    public User getUser(String loginValue) {
        return this.userDao.findByPhone(loginValue);
    }

    @Override
    public User checkAndGet(UserAuthModel userAuth) {
        User user = this.userDao.findByPhone(userAuth.getLoginValue());
        if (user == null) {
            return null;
        }

        if (passwordEncoder.matches(userAuth.getPassWord(), user.getPassWord())) {
            return user;
        }
        return null;

    }
}
