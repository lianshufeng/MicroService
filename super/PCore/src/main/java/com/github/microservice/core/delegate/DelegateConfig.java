package com.github.microservice.core.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Map;

public class DelegateConfig {


    @Bean
    public DelegateHelper delegateHelper() {
        return new DelegateHelper();
    }


    @Autowired
    private void init(ApplicationContext applicationContext, DelegateHelper delegateHelper) {
        Map<String, Object> beanMaps = applicationContext.getBeansWithAnnotation(DelegateMapping.class);
        beanMaps.values().forEach(delegateHelper::delegateMapping);
    }


}
