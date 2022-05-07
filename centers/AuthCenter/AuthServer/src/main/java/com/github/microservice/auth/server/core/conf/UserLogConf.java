package com.github.microservice.auth.server.core.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "user.log")
public class UserLogConf {

    //日志过期时间
    private Long timeOut = 1L * 1000 * 60 * 60 * 24 * 180;

}
