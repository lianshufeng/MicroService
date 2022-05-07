package com.github.microservice.auth.client.event.auth;

import com.github.microservice.auth.client.model.StreamModel;
import org.springframework.context.ApplicationEvent;

public abstract class SuperAuthEvent extends ApplicationEvent {


    public SuperAuthEvent(Object source) {
        super(source);
    }

    /**
     * 取出流模型
     *
     * @return
     */
    public StreamModel getStreamModel() {
        Object source = getSource();
        if (source != null && source instanceof StreamModel) {
            return (StreamModel) source;
        }
        return null;
    }


}
