package com.github.microservice.auth.client.event.auth;

public class EnterpriseUserApplicationEvent extends SuperAuthEvent {


    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public EnterpriseUserApplicationEvent(Object source) {
        super(source);
    }
}
