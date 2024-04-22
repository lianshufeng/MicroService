package com.github.microservice.auth.security.config;

import com.github.microservice.core.mvc.MVCConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;


/**
 * 不拦截web请求
 */
@Order
@Configuration
@Import(MVCConfiguration.class)
public class WebSecurityConfigurer {


}



