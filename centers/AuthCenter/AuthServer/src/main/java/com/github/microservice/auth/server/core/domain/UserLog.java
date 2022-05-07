package com.github.microservice.auth.server.core.domain;

import com.github.microservice.auth.client.model.UserModel;
import com.github.microservice.auth.client.model.UserTokenModel;
import com.github.microservice.auth.security.helper.UserLogHelper;
import com.github.microservice.auth.security.model.EnterpriseUserCacheModel;
import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.*;
import lombok.experimental.Delegate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户日志
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class UserLog extends SuperEntity {

    //应用名
    @Indexed
    private String applicationName;

    //方法
    @Indexed
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

    @Indexed
    private String url;


    //浏览器的UA
    @Indexed
    private String ua;

    //用户的ip
    @Indexed
    private String ip;


    //耗时
    @Indexed
    private long costTime;


    //记录的日志
    @Indexed
    private List<UserLogHelper.LogItem> logs = new ArrayList<>();


    //异常
    @Indexed
    private Map<String, Object> exception;


    //自动过期时间
    @Indexed(expireAfterSeconds = 0)
    private Date TTL;

}
