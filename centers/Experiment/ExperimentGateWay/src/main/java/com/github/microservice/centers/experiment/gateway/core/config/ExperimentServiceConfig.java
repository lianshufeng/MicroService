package com.github.microservice.centers.experiment.gateway.core.config;

import com.github.microservice.centers.experiment.gateway.core.service.ExperimentService;
import com.github.microservice.centers.experiment.gateway.core.service.impl.ExperimentServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExperimentServiceConfig {

    @Bean
    @ConditionalOnMissingBean
    public ExperimentService experimentService(){
        return new ExperimentServiceImpl();
    }

}
