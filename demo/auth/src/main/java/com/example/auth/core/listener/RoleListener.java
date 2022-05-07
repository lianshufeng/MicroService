package com.example.auth.core.listener;

import com.github.microservice.auth.client.event.auth.RoleApplicationEvent;
import com.github.microservice.auth.client.model.StreamModel;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RoleListener implements ApplicationListener<RoleApplicationEvent> {
    @Override
    public void onApplicationEvent(RoleApplicationEvent roleApplicationEvent) {
        StreamModel streamModel = roleApplicationEvent.getStreamModel();
        System.out.println(streamModel);
    }
}
