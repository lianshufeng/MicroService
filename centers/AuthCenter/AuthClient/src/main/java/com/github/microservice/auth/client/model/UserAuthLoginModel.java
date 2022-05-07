package com.github.microservice.auth.client.model;

import com.github.microservice.auth.client.type.DeviceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "userAuthLoginModel", description = "用户权限登陆模型")
public class UserAuthLoginModel extends UserAuthModel {

    //客户端id
    @ApiModelProperty(name = "clientId", required = true, example = "test")
    private String clientId;

    //客户端密钥
    @ApiModelProperty(name = "clientSecret", required = true, example = "123456")
    private String clientSecret;

    //用户设备类型
    @ApiModelProperty(name = "deviceType", required = true, example = "Pc")
    private DeviceType deviceType;

    //用户设备编码
    @ApiModelProperty(name = "deviceUUid", required = false, example = "0000001")
    private String deviceUUid;

    //设备代理
    @ApiModelProperty(name = "deviceUserAgent", required = false, example = "UATest")
    private String deviceUserAgent;

    //设备ip
    @ApiModelProperty(name = "deviceIp", required = false, example = "127.0.0.1")
    private String deviceIp;

    //访问令牌超时时间 (秒)
    @ApiModelProperty(name = "accessTokenTimeOut", required = false, example = "259200")
    private Long accessTokenTimeOut;

    //刷新令牌超时时间 (秒)
    @ApiModelProperty(name = "refreshTokenTimeOut", required = false, example = "31536000")
    private Long refreshTokenTimeOut;


}
