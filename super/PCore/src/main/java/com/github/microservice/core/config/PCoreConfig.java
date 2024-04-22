package com.github.microservice.core.config;

import com.github.microservice.core.helper.*;
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


    @Bean
    public ScanHelper scanHelper() {
        return new ScanHelper();
    }


    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }


}
