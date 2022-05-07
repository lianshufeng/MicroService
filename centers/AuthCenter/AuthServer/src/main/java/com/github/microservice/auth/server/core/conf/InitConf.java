package com.github.microservice.auth.server.core.conf;

import com.github.microservice.auth.client.type.LoginType;
import com.github.microservice.auth.security.type.AuthType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 初始化配置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "init")
public class InitConf {

    //初始化用户信息
    private User[] user;

    //初始化企业信息
    private ApplicationClient[] client;

    //企业信息
    private Enterprise[] enterprise;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Enterprise {
        //企业名
        private String name;
        //权限类型
        private AuthType authType = AuthType.Enterprise;
        //备注
        private String remark;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        //类型
        private LoginType loginType;

        //登陆名
        private String loginValue;

        //密钥
        private String passWord;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicationClient {


        //客户端id
        private String clientId;

        //密钥
        private String secret;

        //权限类型
        private Set<String> authorizedGrantTypes;

    }

}
