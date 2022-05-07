package com.github.microservice.auth.server.core.domain;

import com.github.microservice.auth.client.type.DeviceType;
import com.github.microservice.auth.client.type.LoginType;
import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 用户登陆日志
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginLog extends SuperEntity {

    @Indexed
    @DBRef(lazy = true)
    private User user;

    //用户令牌
    @Indexed
    private String accessToken;

    //用户刷新令牌
    @Indexed
    private String refreshToken;

    //客户端id
    @Indexed
    private String clientId;


    //用户设备类型
    @Indexed
    private DeviceType deviceType;

    //用户设备编码
    @Indexed
    private String deviceUUid;

    //设备代理
    @Indexed
    private String deviceUserAgent;

    //设备ip
    @Indexed
    private String deviceIp;

    //登陆类型
    @Indexed
    private LoginType loginType;

    //登陆名
    @Indexed
    private String loginName;

    //访问令牌超时时间
    private Integer accessTokenTimeOut;

    //刷新令牌超时时间
    private Integer refreshTokenTimeOut;


}
