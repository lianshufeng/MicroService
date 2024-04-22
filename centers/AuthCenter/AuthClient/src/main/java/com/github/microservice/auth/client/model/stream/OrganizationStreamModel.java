package com.github.microservice.auth.client.model.stream;

import com.github.microservice.auth.client.type.AuthEventType;
import lombok.NoArgsConstructor;

/**
 * 用户流模型
 */
@NoArgsConstructor
public class OrganizationStreamModel extends SuperStreamModel {

    public OrganizationStreamModel(AuthEventType eventType, String id) {
        super(eventType, id);
    }

    public OrganizationStreamModel(AuthEventType eventType, String id, Object[] items) {
        super(eventType, id, items);
    }


}
