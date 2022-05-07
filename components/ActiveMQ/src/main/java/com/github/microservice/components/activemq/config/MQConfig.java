package com.github.microservice.components.activemq.config;

import com.github.microservice.core.config.PCoreConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import(PCoreConfig.class)
@ComponentScan("com.github.microservice.components.activemq")
public class MQConfig {

}
