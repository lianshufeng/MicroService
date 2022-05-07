package com.example.auth.core.config;

import com.github.microservice.auth.config.AuthClientConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AuthClientConfiguration.class)
public class AuthConfig {


}
