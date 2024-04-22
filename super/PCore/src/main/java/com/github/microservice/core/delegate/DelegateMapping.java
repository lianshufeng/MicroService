package com.github.microservice.core.delegate;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DelegateMapping {

    Class<?>[] types() default {};

}
