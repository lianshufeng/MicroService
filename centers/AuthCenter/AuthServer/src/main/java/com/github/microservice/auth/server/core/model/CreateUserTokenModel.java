package com.github.microservice.auth.server.core.model;

import com.github.microservice.auth.client.type.DeviceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserTokenModel {

    //用户id
    private String userId;

    //设备类型
    private DeviceType deviceType;

    //客户端id
    private String clientId;

    //访问令牌超时时间
    private int accessTokenTimeOut;

}
