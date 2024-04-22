package com.github.microservice.auth.security.annotations;


import com.github.microservice.auth.security.type.AuthType;

import java.lang.annotation.*;

/**
 * 资源权限注解
 */

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Repeatable(value = ResourceAuths.class)
public @interface ResourceAuth {


    /**
     * 资源名称
     *
     * @return
     */
    String[] value();

    /**
     * 资源类型,注意默认为用户权限
     *
     * @return
     */
    AuthType type() default AuthType.Platform;


    /**
     * 描述
     *
     * @return
     */
    String remark() default "";


}
