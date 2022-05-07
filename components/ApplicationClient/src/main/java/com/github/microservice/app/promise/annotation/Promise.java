package com.github.microservice.app.promise.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Promise {

    /**
     * 补偿方法
     *
     * @return
     */
    String value();

}