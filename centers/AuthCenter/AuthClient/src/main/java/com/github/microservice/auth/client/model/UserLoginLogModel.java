package com.github.microservice.auth.client.model;

import com.github.microservice.auth.client.type.DeviceType;
import com.github.microservice.auth.client.type.LoginType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginLogModel {

    //用户id
    private String uid;

    //访问令牌
    private String accessToken;

    //用户刷新令牌
    private String refreshToken;

    //登陆客户端
    private String clientId;

    //用户设备类型
    private DeviceType deviceType;

    //用户设备编码
    private String deviceUUid;

    //设备代理
    private String deviceUserAgent;

    //设备ip
    private String deviceIp;

    //登陆类型
    private LoginType loginType;

    //登陆名
    private String loginName;

    //创建时间
    private long createTime;
}
