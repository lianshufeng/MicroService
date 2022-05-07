package com.github.microservice.auth.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 令牌模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginTokenModel {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;
    private String scope;
}
