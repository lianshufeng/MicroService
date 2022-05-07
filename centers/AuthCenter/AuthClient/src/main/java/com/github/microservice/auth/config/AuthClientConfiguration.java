package com.github.microservice.auth.config;

import com.github.microservice.auth.client.config.MemoryCacheConfig;
import com.github.microservice.auth.security.config.SecurityConfig;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 外部引用接口请直接引用本接口
 */

@Configuration
@EnableFeignClients("com.github.microservice.auth.client.service")
@Import({MemoryCacheConfig.class, SecurityConfig.class})
@ComponentScan({
        "com.github.microservice.auth.client"
})
public class AuthClientConfiguration {


}
