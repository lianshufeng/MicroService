package com.github.microservice.app.promise.hystrix;

import com.github.microservice.app.promise.model.PromiseModel;
import com.github.microservice.core.helper.SpringBeanHelper;
import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.contrib.javanica.command.AbstractHystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.CommandActions;
import com.netflix.hystrix.contrib.javanica.command.MethodExecutionAction;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PromiseHystrixCommandExecutionHook extends HystrixCommandExecutionHook {

    @Value("${spring.application.name}")
    private String _appName;

    @Autowired
    private PromiseStreamHelper promiseStreamHelper;

    @Autowired
    private SpringBeanHelper springBeanHelper;


    @Override
    public <T> Exception onFallbackError(HystrixInvokable<T> commandInstance, Exception e) {
        recordFallBack(commandInstance);
        return super.onFallbackError(commandInstance, e);
    }


    /**
     * 发送到mq里进行补偿
     *
     * @param commandInstance
     */
    @SneakyThrows
    private void recordFallBack(HystrixInvokable commandInstance) {
        if (!(commandInstance instanceof AbstractHystrixCommand)) {
            return;
        }
        AbstractHystrixCommand command = ((AbstractHystrixCommand) commandInstance);
        Field commandActionsField = AbstractHystrixCommand.class.getDeclaredField("commandActions");
        commandActionsField.setAccessible(true);
        Object commandActionsValue = commandActionsField.get(command);
        MethodExecutionAction commandAction = (MethodExecutionAction) ((CommandActions) commandActionsValue).getFallbackAction();


        PromiseModel promiseModel = new PromiseModel();

        //应用名
        promiseModel.setApplicationName(_appName);
        //SpringBean的名称
        promiseModel.setBeanName(springBeanHelper.getBeanName(commandAction.getObject()));
        //方法
        promiseModel.setMethodName(commandAction.getMethod().getName());

        //方法的参数
        Optional.ofNullable(commandAction.getMethod().getParameterTypes()).ifPresent((types) -> {
            promiseModel.setParameterTypes(
                    Arrays.stream(types).map((it) -> {
                        return it.getName();
                    }).collect(Collectors.toList()).toArray(new String[0])
            );
        });

        //参数
        promiseModel.setParameter(commandAction.getArgs());


        this.promiseStreamHelper.addFallback(promiseModel);
    }


}
