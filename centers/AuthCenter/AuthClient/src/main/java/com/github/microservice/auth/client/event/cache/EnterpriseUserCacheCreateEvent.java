package com.github.microservice.auth.client.event.cache;

import com.github.microservice.auth.security.model.EnterpriseUserCacheModel;
import org.springframework.context.ApplicationEvent;

public class EnterpriseUserCacheCreateEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public EnterpriseUserCacheCreateEvent(Object source) {
        super(source);
    }


    /**
     * 获取 EnterpriseUserCacheModel
     * @return
     */
    public EnterpriseUserCacheModel getEnterpriseUserCacheModel() {
        return (EnterpriseUserCacheModel) this.getSource();
    }

}
