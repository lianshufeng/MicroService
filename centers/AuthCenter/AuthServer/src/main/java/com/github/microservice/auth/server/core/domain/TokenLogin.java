package com.github.microservice.auth.server.core.domain;

import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * 令牌登陆
 */
@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TokenLogin extends SuperEntity {


    //用户
    @Indexed
    @DBRef(lazy = true)
    private User user;

    //令牌
    @Indexed(unique = true)
    private String token;

    //验证的值
    private String value;


    //现在的校验次数
    @Indexed
    private int currentCheckCount;

    //最大校验次数
    @Indexed
    private int maxCheckCount;


    //过期时间
    @Indexed(expireAfterSeconds = 0)
    private Date TTL;


}
