package com.fast.dev.demo.core.config;

import com.github.microservice.components.swagger.config.SwaggerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SwaggerConfiguration.class)
public class DocApiConfig {




}
