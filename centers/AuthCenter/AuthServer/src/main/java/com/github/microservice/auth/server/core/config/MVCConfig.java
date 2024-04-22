package com.github.microservice.auth.server.core.config;

import com.github.microservice.core.mvc.MVCConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MVCConfiguration.class)
public class MVCConfig {
}
