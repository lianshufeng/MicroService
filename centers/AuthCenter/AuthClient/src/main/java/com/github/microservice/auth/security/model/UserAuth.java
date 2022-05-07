package com.github.microservice.auth.security.model;

import com.github.microservice.auth.client.model.UserTokenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

/**
 * 用户权限
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuth extends UserTokenModel {


    private Set<String> auths;

    /**
     * 详情
     */
    private Map<String, Object> details;


}
