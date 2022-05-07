package com.github.microservice.auth.security.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WebSecurityConfigurer.class, MethodSecurityConfig.class})
@ComponentScan({"com.github.microservice.auth.security"})
public class SecurityConfig {


}
