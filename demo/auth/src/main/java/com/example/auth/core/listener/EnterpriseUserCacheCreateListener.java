package com.example.auth.core.listener;

import com.github.microservice.auth.client.event.cache.OrganizationUserCacheCreateEvent;
import com.github.microservice.auth.security.model.OrganizationUserCacheModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(100)
@Component
public class EnterpriseUserCacheCreateListener implements ApplicationListener<OrganizationUserCacheCreateEvent> {

    @Override
    public void onApplicationEvent(OrganizationUserCacheCreateEvent event) {
        OrganizationUserCacheModel organizationUserCacheModel = event.getOrganizationUserCacheModel();
        String resourceNames = organizationUserCacheModel.getOrganization() + " -> " + organizationUserCacheModel.getUser();
        organizationUserCacheModel.other().put("res", resourceNames);
        log.info("EnterpriseUserCacheCreate -- {}", resourceNames);
    }
}
