package com.github.microservice.core.config;

import com.github.microservice.core.helper.JsonHelper;
import com.github.microservice.core.helper.SpringBeanHelper;
import com.github.microservice.core.helper.ViewHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PCoreConfig {


    @Bean
    public JsonHelper jsonHelper() {
        return new JsonHelper();
    }


    @Bean
    public ViewHelper viewHelper() {
        return new ViewHelper();
    }


    @Bean
    public SpringBeanHelper springBeanHelper() {
        return new SpringBeanHelper();
    }

}
