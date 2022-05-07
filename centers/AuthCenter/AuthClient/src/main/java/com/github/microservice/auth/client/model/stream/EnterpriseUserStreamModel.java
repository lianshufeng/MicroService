package com.github.microservice.auth.client.model.stream;

import com.github.microservice.auth.client.type.AuthEventType;
import lombok.NoArgsConstructor;

/**
 * 企业用户
 */
@NoArgsConstructor
public class EnterpriseUserStreamModel extends SuperStreamModel {

    public EnterpriseUserStreamModel(AuthEventType eventType, String id) {
        super(eventType, id);
    }

    public EnterpriseUserStreamModel(AuthEventType eventType, String id, Object[] items) {
        super(eventType, id, items);
    }


}
