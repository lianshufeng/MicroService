package com.github.microservice.auth.config;

import com.github.microservice.auth.client.config.MemoryCacheConfig;
import com.github.microservice.auth.client.helper.AuthEventStreamHelper;
import com.github.microservice.auth.client.helper.TokenEventStreamHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 外部引用接口请直接引用本接口
 */

@Configuration
@Import({MemoryCacheConfig.class})
public class AuthServerConfiguration {

    @Bean
    public AuthEventStreamHelper authEventStreamHelper() {
        return new AuthEventStreamHelper();
    }


    @Bean
    public TokenEventStreamHelper tokenEventStreamHelper() {
        return new TokenEventStreamHelper();
    }


}
