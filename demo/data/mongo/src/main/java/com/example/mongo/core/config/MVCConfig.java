package com.example.mongo.core.config;

import com.github.microservice.core.mvc.MVCConfiguration;
import com.github.microservice.core.mvc.MVCResponseConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({MVCConfiguration.class, MVCResponseConfiguration.class})
@Configuration
public class MVCConfig {
}
