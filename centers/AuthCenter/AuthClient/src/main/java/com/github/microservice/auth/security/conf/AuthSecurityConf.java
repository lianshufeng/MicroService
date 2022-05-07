package com.github.microservice.auth.security.conf;

import com.github.microservice.core.mvc.MVCConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "auth.security")
public class AuthSecurityConf {


    // 需要拦截方法的URL
    private String[] needSecurityMethodUrl = new String[]{"/**"};

    // 在拦截的URL中排除不拦截的URL
    private String[] excludeSecurityMethodUrl = new String[]{"/" + MVCConfiguration.StaticResources + "/**", "/error"};

    //  用户中心的应用名称
    private String authCenter = "authserver";

}
