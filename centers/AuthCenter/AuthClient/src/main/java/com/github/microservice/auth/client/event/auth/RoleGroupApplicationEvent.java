package com.github.microservice.auth.client.event.auth;

public class RoleGroupApplicationEvent extends SuperAuthEvent {


    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public RoleGroupApplicationEvent(Object source) {
        super(source);
    }
}
