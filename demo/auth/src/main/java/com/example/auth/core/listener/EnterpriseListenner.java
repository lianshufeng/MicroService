package com.example.auth.core.listener;

import com.github.microservice.auth.client.event.auth.OrganizationApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EnterpriseListenner implements ApplicationListener<OrganizationApplicationEvent> {
    @Override
    public void onApplicationEvent(OrganizationApplicationEvent enterpriseApplicationEvent) {
        System.out.println("thread : -> " + Thread.currentThread());
    }
}
