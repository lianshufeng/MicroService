package com.github.microservice.auth.client.model.stream;

import com.github.microservice.auth.client.type.AuthEventType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RoleGroupUserStreamModel extends SuperStreamModel{

    public RoleGroupUserStreamModel(AuthEventType eventType, String id) {
        super(eventType, id);
    }

    public RoleGroupUserStreamModel(AuthEventType eventType, String id, Object[] items) {
        super(eventType, id, items);
    }
}
