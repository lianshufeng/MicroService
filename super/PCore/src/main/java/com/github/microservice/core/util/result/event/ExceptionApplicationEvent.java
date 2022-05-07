package com.github.microservice.core.util.result.event;

import org.springframework.context.ApplicationEvent;

public class ExceptionApplicationEvent extends ApplicationEvent {
    public ExceptionApplicationEvent(Object source) {
        super(source);
    }


    /**
     * 获取异常
     *
     * @return
     */
    public Exception getException() {
        return (Exception) this.source;
    }


}
