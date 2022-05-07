package com.github.microservice.auth.server.core.conf;

import com.github.microservice.auth.client.type.DeviceType;
import com.github.microservice.auth.client.type.LoginType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "auth")
public class AuthConf {

    //默认的登陆类型
    private LoginType defaultLoginType = LoginType.Phone;

    //默认的登陆类型
    private DeviceType defaultDeviceType = DeviceType.Pc;

    //访问令牌的时效性
    private int accessTokenValiditySeconds = 60 * 60 * 24;

    //刷新令牌时效性
    private int refreshTokenValiditySeconds = 60 * 60 * 24 * 30 * 12;

    //限制一台设备登陆
    private boolean onlyOneDeviceLogin = true;


}
