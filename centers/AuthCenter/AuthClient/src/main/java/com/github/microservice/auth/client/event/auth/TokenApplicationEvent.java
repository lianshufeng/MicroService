package com.github.microservice.auth.client.event.auth;

public class TokenApplicationEvent extends SuperAuthEvent {


    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public TokenApplicationEvent(Object source) {
        super(source);
    }
}
