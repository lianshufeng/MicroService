package com.github.microservice.auth.client.model;

import com.github.microservice.auth.security.type.AuthType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseUserModel {

    //企业id
    private String enterprise;

    //权限类型
    private AuthType authType;

    //用户id
    private String user;

    //权限
    private Set<String> auth;

    //身份
    private Set<String> identity;

    //角色
    private Set<String> roleId;

    //角色组
    private Set<String> roleGroupId;
}
