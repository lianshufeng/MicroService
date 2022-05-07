package com.github.microservice.auth.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户令牌缓存
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenModel {

    //访问令牌
    private String accessToken;

    //用户id
    private String uid;

    //到期时间单位秒
    private Long expireTime;


}
