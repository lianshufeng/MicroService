package com.example.auth.core.listener;

import com.github.microservice.auth.client.event.auth.EnterpriseApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EnterpriseListenner implements ApplicationListener<EnterpriseApplicationEvent> {
    @Override
    public void onApplicationEvent(EnterpriseApplicationEvent enterpriseApplicationEvent) {
        System.out.println("thread : -> " + Thread.currentThread());
    }
}
