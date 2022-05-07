package com.github.microservice.auth.server.core.config;

import com.github.microservice.auth.config.AuthServerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AuthServerConfiguration.class)
public class AuthServerConfig {
}
