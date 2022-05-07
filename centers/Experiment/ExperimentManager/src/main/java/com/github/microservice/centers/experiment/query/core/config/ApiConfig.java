package com.github.microservice.centers.experiment.query.core.config;

import com.github.microservice.components.swagger.config.SwaggerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SwaggerConfiguration.class)
public class ApiConfig {
}
