package com.github.microservice.components.swagger.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
public class SwaggerConfiguration {

    @Bean
    public SwaggerUiConfigParameters swaggerUiConfigParameters(SwaggerUiConfigProperties swaggerUiConfigProperties) {
        return new SwaggerUiConfigParameters(swaggerUiConfigProperties);
    }


    @ConditionalOnMissingBean
    @Bean
    public OperationCustomizer addCustomGlobalHeader() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            operation.addParametersItem(new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .required(false)
                    .schema(new StringSchema()._default(""))
                    .name("accessToken"));

            operation.addParametersItem(new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .required(false)
                    .schema(new StringSchema()._default(""))
                    .name("oid"));

            return operation;
        };
    }


//    @Bean
//    public OpenAPI customOpenAPI() {
////        new Components().addSecuritySchemes("accessToken", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT"))
//        return new OpenAPI().components(
//                new Components().addSecuritySchemes("bearer", new SecurityScheme().type(SecurityScheme.Type.HTTP))
//        );
//    }


}