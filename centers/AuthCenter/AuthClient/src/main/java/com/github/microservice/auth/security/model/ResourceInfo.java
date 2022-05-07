package com.github.microservice.auth.security.model;

import com.github.microservice.auth.security.type.AuthType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceInfo {

    //资源名
    private String name;

    //描述
    private String remark;

    //资源权限
    private AuthType authType;


}
