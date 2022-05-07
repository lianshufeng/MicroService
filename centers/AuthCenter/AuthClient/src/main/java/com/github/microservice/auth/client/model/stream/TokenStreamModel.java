package com.github.microservice.auth.client.model.stream;

import com.github.microservice.auth.client.type.AuthEventType;
import lombok.NoArgsConstructor;

/**
 * 用户流模型
 */
@NoArgsConstructor
public class TokenStreamModel extends SuperStreamModel {

    public TokenStreamModel(AuthEventType eventType, String id) {
        super(eventType, id);
    }

    public TokenStreamModel(AuthEventType eventType, String id, Object[] items) {
        super(eventType, id, items);
    }
}
