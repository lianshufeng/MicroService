package com.github.microservice.demo.experiment.service1.core.config;

import com.github.microservice.core.mvc.MVCConfiguration;
import com.github.microservice.core.mvc.MVCResponseConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({MVCConfiguration.class, MVCResponseConfiguration.class})
@Configuration
public class MVCConfig {
}
