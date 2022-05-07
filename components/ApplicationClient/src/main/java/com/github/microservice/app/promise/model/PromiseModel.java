package com.github.microservice.app.promise.model;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PromiseModel {

    //应用名
    private String applicationName;

    //补偿的对象(仅为spring的Bean的名称)
    private String beanName;

    //补偿的方法名
    private String methodName;

    //参数类型
    private String[] parameterTypes;

    //参数
    private Object[] parameter;

}
