package com.github.microservice.auth.server.core.auth.endpoint;

import com.github.microservice.auth.client.type.DeviceType;
import com.github.microservice.auth.client.type.GrantType;
import com.github.microservice.auth.client.type.LoginType;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthProcess {

    //登陆名
    private String loginName;

    //密码
    private String passWord;

    //类型
    private LoginType loginType;

    //设备类型
    private DeviceType deviceType;

    //设备id
    private String deviceUUid;

    //设备代理信息
    private String deviceUserAgent;

    //设备ip
    private String deviceIp;

    //类型
    private GrantType grantType;

    //用户id, 仅在认证成功后设置
    private String uid;

    //客户端id
    private String clientId;

    //访问令牌超时时间
    private Integer accessTokenTimeOut;

    //刷新令牌超时时间
    private Integer refreshTokenTimeOut;


    //参数
    private Map<String, String> parameters;


}
