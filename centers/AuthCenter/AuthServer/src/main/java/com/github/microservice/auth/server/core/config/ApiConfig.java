package com.github.microservice.auth.server.core.config;

import com.github.microservice.components.swagger.config.SwaggerConfiguration;
import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
//@Import(SwaggerConfiguration.class)
public class ApiConfig extends SwaggerConfiguration {

    @Override
    @Bean
    @ConditionalOnMissingBean
    public OperationCustomizer addCustomGlobalHeader() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            return operation;
        };
    }
}
