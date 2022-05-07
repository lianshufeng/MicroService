package com.github.microservice.auth.server.core.domain;

import com.github.microservice.auth.client.type.DeviceType;
import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class UserToken extends SuperEntity {

    // 用户id
    @Indexed
    @DBRef(lazy = true)
    private User user;

    //刷新令牌
    @Indexed(unique = true)
    private String refreshToken;

    //设备类型
    @Indexed
    private DeviceType deviceType;

    //设备id
    @Indexed
    private String deviceUUid;

    //设备UA
    private String deviceUserAgent;

    //设备ip
    private String deviceIp;

    //客户端id
    @Indexed
    private String clientId;

    //过期时间
    @Indexed(expireAfterSeconds = 0)
    private Date TTL;


}
