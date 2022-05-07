package com.github.microservice.auth.security.listener;

import com.github.microservice.auth.security.helper.UserLogHelper;
import com.github.microservice.core.util.result.event.ExceptionApplicationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 系统异常
 */
@Component
public class SystemExceptionApplicationListener implements ApplicationListener<ExceptionApplicationEvent> {

    @Autowired
    private UserLogHelper userLogHelper;


    @Override
    public void onApplicationEvent(ExceptionApplicationEvent exceptionApplicationEvent) {
        final Exception exception = exceptionApplicationEvent.getException();
        Optional.ofNullable(userLogHelper.getLogItems()).ifPresent((it) -> {
            it.setException(exception);
        });
    }
}
