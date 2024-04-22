package com.github.microservice.auth.client.model;

import com.github.microservice.auth.client.type.ResourceType;
import com.github.microservice.auth.security.type.AuthType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResourcesNameModel {

    //资源类型
    @Schema(name = "resourceType", example = "AuthName")
    private ResourceType resourceType;

    //权限类型
    @Schema(name = "authType", example = "Enterprise")
    private AuthType authType;

    //资源名
    @Schema(name = "name", example = "test1")
    private String name;

    //备注
    @Schema(name = "remark", example = "这个是备注")
    private String remark;
}
