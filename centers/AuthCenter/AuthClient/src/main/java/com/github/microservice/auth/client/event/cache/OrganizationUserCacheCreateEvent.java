package com.github.microservice.auth.client.event.cache;

import com.github.microservice.auth.security.model.OrganizationUserCacheModel;
import org.springframework.context.ApplicationEvent;

public class OrganizationUserCacheCreateEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public OrganizationUserCacheCreateEvent(Object source) {
        super(source);
    }


    /**
     * @return
     */
    public OrganizationUserCacheModel getOrganizationUserCacheModel() {
        return (OrganizationUserCacheModel) this.getSource();
    }

}
