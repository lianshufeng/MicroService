package com.github.microservice.auth.security.helper;

import com.github.microservice.auth.security.type.AuthType;
import org.springframework.stereotype.Component;

/**
 * 用户权限注册
 */
@Component
public class UserAuthHelper {

    // 默认的用户类型
    private AuthType DefaultUserAuthType = AuthType.User;

    //默认的角色组id
    private String roleGroupId = null;


    /**
     * 获取用户权限
     */
    private void requestUserAuth() {

    }


    public void addUser(){

    }



}
