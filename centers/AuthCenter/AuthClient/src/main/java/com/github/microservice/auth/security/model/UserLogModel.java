package com.github.microservice.auth.security.model;

import com.github.microservice.auth.client.model.UserModel;
import com.github.microservice.auth.client.model.UserTokenModel;
import com.github.microservice.auth.security.helper.UserLogHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class UserLogModel {


    //应用名
    @Getter
    @Setter
    private String applicationName;

    //方法
    @Getter
    @Setter
    private String method;


    //用户登陆信息
    @Delegate
    private UserModel userModel = new UserModel();

    //用户令牌信息
    @Delegate
    private UserTokenModel userTokenModel = new UserTokenModel();


    //企业信息
    @Delegate
    private EnterpriseUserCacheModel enterpriseUserModel = new EnterpriseUserCacheModel();


    @Getter
    @Setter
    private String url;


    //浏览器的UA
    @Getter
    @Setter
    private String ua;

    //用户的ip
    @Getter
    @Setter
    private String ip;


    //耗时
    @Getter
    @Setter
    private long costTime;


    //记录的日志
    @Getter
    @Setter
    private List<UserLogHelper.LogItem> logs = new ArrayList<>();

    //创建时间
    @Setter
    @Getter
    private long createTime;

    //异常
    @Getter
    @Setter
    private Map<String, Object> exception;


}
