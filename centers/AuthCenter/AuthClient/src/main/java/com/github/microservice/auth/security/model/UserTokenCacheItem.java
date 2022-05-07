package com.github.microservice.auth.security.model;

import com.github.microservice.auth.client.model.UserModel;
import com.github.microservice.auth.client.model.UserTokenModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;

import java.util.Set;

/**
 * 企业用户缓存项
 */
public class UserTokenCacheItem {

    //用户登陆信息
    @Delegate
    private UserModel userModel = new UserModel();

    //用户令牌信息
    @Delegate
    private UserTokenModel userTokenModel = new UserTokenModel();


    //用户所在的附属企业
    @Getter
    @Setter
    private Set<String> affiliatesEnterprise;


}
