package com.github.microservice.auth.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleGroupUserModel {

    //企业
    private String enterprise;

    //角色组
    private String roleGroup;

    //拥护
    private String user;
}
