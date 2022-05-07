package com.github.microservice.auth.client.event.auth;

public class UserRoleApplicationEvent extends SuperAuthEvent {


    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public UserRoleApplicationEvent(Object source) {
        super(source);
    }
}
