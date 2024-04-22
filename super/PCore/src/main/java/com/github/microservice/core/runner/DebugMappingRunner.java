package com.github.microservice.core.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collection;
import java.util.Map;


@Slf4j
public class DebugMappingRunner implements ApplicationRunner {


    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        final Collection<RequestMappingHandlerMapping> requestMappingHandlerMappings = applicationContext.getBeansOfType(RequestMappingHandlerMapping.class).values();
        if (requestMappingHandlerMappings.size() == 0) {
            return;
        }
        requestMappingHandlerMappings.forEach((requestMappingHandlerMapping) -> {
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
            handlerMethods.entrySet().forEach(it -> {
                log.debug("[RequestMapping] : {} - {}", it.getKey(), it.getValue());
            });
        });
    }
}
