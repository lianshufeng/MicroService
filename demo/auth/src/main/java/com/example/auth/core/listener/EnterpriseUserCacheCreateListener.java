package com.example.auth.core.listener;

import com.github.microservice.auth.client.event.cache.EnterpriseUserCacheCreateEvent;
import com.github.microservice.auth.security.model.EnterpriseUserCacheModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(100)
@Component
public class EnterpriseUserCacheCreateListener implements ApplicationListener<EnterpriseUserCacheCreateEvent> {

    @Override
    public void onApplicationEvent(EnterpriseUserCacheCreateEvent event) {
        EnterpriseUserCacheModel enterpriseUserCacheModel = event.getEnterpriseUserCacheModel();
        String resourceNames = enterpriseUserCacheModel.getEnterprise() + " -> " + enterpriseUserCacheModel.getUser();
        enterpriseUserCacheModel.other().put("res", resourceNames);
        log.info("EnterpriseUserCacheCreate -- {}", resourceNames);
    }
}
