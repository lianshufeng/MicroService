package com.github.microservice.app.promise.aop;

import com.github.microservice.app.promise.annotation.Promise;
import com.github.microservice.app.promise.hystrix.PromiseStreamHelper;
import com.github.microservice.app.promise.model.PromiseModel;
import com.github.microservice.core.helper.SpringBeanHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class PromiseAop {

    @Value("${spring.application.name}")
    private String _appName;

    @Autowired
    private SpringBeanHelper springBeanHelper;

    @Autowired
    private PromiseStreamHelper promiseStreamHelper;

    @Pointcut("@annotation(com.github.microservice.app.promise.annotation.Promise) ")
    public void promiseAop() {
    }


    @AfterThrowing(throwing = "ex", pointcut = "promiseAop()")
    public void promiseAopException(JoinPoint point, Exception ex) {
        if (!(point instanceof MethodInvocationProceedingJoinPoint)) {
            return;
        }
        //取出切点方法
        final MethodInvocationProceedingJoinPoint joinPoint = (MethodInvocationProceedingJoinPoint) point;
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final Method method = methodSignature.getMethod();
        //取出注解
        final Promise promise = method.getAnnotation(Promise.class);
        if (!StringUtils.hasText(promise.value())) {
            log.error("无效的补偿方法 : {}", method);
            return;
        }
        //寻找补偿方法
        final Method promiseMethod = MethodUtils.getMatchingMethod(joinPoint.getThis().getClass(), promise.value(), method.getParameterTypes());
        if (promiseMethod == null) {
            log.error("寻找补偿方法失败 : {}", method);
            return;
        }


        final PromiseModel promiseModel = new PromiseModel();
        //应用名
        promiseModel.setApplicationName(_appName);
        //bean的名称
        promiseModel.setBeanName(this.springBeanHelper.getBeanName(joinPoint.getThis()));
        //方法
        promiseModel.setMethodName(promiseMethod.getName());
        //方法的参数
        Optional.ofNullable(promiseMethod.getParameterTypes()).ifPresent((types) -> {
            promiseModel.setParameterTypes(
                    Arrays.stream(types).map((it) -> {
                        return it.getName();
                    }).collect(Collectors.toList()).toArray(new String[0])
            );
        });
        //参数
        promiseModel.setParameter(joinPoint.getArgs());

        this.promiseStreamHelper.addFallback(promiseModel);
    }


}
