package com.github.ms.demo.app.core.config;

import com.github.microservice.core.mvc.MVCConfiguration;
import com.github.microservice.core.mvc.MVCRequestConfiguration;
import com.github.microservice.core.mvc.MVCResponseConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MVCConfiguration.class)
public class MVCConfig {
}
