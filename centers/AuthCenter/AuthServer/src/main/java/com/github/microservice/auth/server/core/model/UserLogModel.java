package com.github.microservice.auth.server.core.model;

import com.github.microservice.auth.client.model.UserModel;
import com.github.microservice.auth.client.model.UserTokenModel;
import com.github.microservice.auth.security.helper.UserLogHelper;
import com.github.microservice.auth.security.model.OrganizationUserCacheModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogModel {

    //应用名
    private String applicationName;

    //方法
    private String method;


    //用户登陆信息
    private UserModel userModel = new UserModel();

    //用户令牌信息
    private UserTokenModel userTokenModel = new UserTokenModel();


    //企业信息
    private OrganizationUserCacheModel organizationUserCacheModel = new OrganizationUserCacheModel();


    private String url;


    //浏览器的UA
    private String ua;

    //用户的ip
    private String ip;


    //耗时
    private long costTime;


    //记录的日志
    private List<UserLogHelper.LogItem> logs = new ArrayList<>();


    //异常
    private Map<String, Object> exception;

    //自动过期时间
    private Date TTL;

}
