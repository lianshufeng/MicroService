package com.github.microservice.auth.server.core.config;

import com.github.microservice.core.mvc.MVCConfiguration;
import com.github.microservice.core.mvc.MVCResponseConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MVCConfiguration.class, MVCResponseConfiguration.class})
public class MVCConfig {
}
