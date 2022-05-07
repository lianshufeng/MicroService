package com.github.microservice.auth.server.core.service.user;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.model.UserAuthModel;
import com.github.microservice.auth.client.type.LoginType;
import com.github.microservice.auth.server.core.domain.User;

public interface LocalUserService {


    /**
     * 登陆类型
     *
     * @return
     */
    LoginType type();


    /**
     * 添加用户
     *
     * @return
     */
    ResultContent<String> add(UserAuthModel userAuth);


    /**
     * 查询用户对象
     *
     * @param loginValue
     * @return
     */
    User getUser(String loginValue);


    /**
     * 校验用户的账号密码是否正确
     *
     * @return
     */
    User checkAndGet(UserAuthModel userAuth);

}
