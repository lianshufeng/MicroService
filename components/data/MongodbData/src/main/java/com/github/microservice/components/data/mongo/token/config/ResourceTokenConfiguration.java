package com.github.microservice.components.data.mongo.token.config;

import com.github.microservice.components.data.mongo.token.service.ResourceTokenService;
import com.github.microservice.components.data.mongo.token.service.impl.ResourceTokenServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.github.microservice.components.data.mongo.token.dao"})
public class ResourceTokenConfiguration {

    @Bean
    public ResourceTokenService resourceTokenService() {
        return new ResourceTokenServiceImpl();
    }

}
