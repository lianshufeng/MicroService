package com.github.microservice.auth.security.model;

import com.github.microservice.auth.security.type.AuthType;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceAuthModel {
    //资源名
    private String name;

    //资源权限
    private AuthType authType;
}
