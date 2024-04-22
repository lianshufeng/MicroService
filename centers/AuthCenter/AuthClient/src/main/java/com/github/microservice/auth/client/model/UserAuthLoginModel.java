package com.github.microservice.auth.client.model;

import com.github.microservice.auth.client.type.DeviceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "userAuthLoginModel", description = "用户权限登陆模型")
public class UserAuthLoginModel extends UserAuthModel {

    //客户端id
    @Schema(name = "clientId", required = true, example = "test")
    private String clientId;

    //客户端密钥
    @Schema(name = "clientSecret", required = true, example = "123456")
    private String clientSecret;

    //用户设备类型
    @Schema(name = "deviceType", required = true, example = "Pc")
    private DeviceType deviceType;

    //用户设备编码
    @Schema(name = "deviceUUid", required = false, example = "0000001")
    private String deviceUUid;

    //设备代理
    @Schema(name = "deviceUserAgent", required = false, example = "UATest")
    private String deviceUserAgent;

    //设备ip
    @Schema(name = "deviceIp", required = false, example = "127.0.0.1")
    private String deviceIp;

    //访问令牌超时时间 (秒)
    @Schema(name = "accessTokenTimeOut", required = false, example = "259200")
    private Long accessTokenTimeOut;

    //刷新令牌超时时间 (秒)
    @Schema(name = "refreshTokenTimeOut", required = false, example = "31536000")
    private Long refreshTokenTimeOut;


}
