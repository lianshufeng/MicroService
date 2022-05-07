package com.github.microservice.auth.security.aop;

import com.github.microservice.auth.security.helper.UserLogHelper;
import groovy.util.logging.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 权限钩子：用于扩展权限的缓存时间
 */
@Log
@Aspect
@Component
public class ResourceAuthAop {

    @Autowired
    private UserLogHelper userLogHelper;


    @Pointcut("@annotation(com.github.microservice.auth.security.annotations.ResourceAuths) || @annotation(com.github.microservice.auth.security.annotations.ResourceAuth)")
    public void methodPoint() {

    }

    @Before(value = "methodPoint()")
    public void beforeMethod(JoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        UserLogHelper.LogItems logItems = this.userLogHelper.getLogItems();
        logItems.setSendLog(true);
        logItems.setMethod(methodName(method));
    }


    /**
     * 获取方法名
     *
     * @param method
     * @return
     */
    private static String methodName(Method method) {
        return method.getDeclaringClass().getTypeName() + "." + method.getName();
    }


}
