package com.github.microservice.app.annotation;

import com.github.microservice.app.core.config.ConsulRegisterConfig;
import com.github.microservice.app.core.config.FeignConfig;
import com.github.microservice.app.core.config.PromiseConfig;
import com.github.microservice.app.core.config.RestTemplateConfig;
import com.github.microservice.app.stream.StreamConfig;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 应用客户端注解
 */


//引用的非公开变量不能继承
@EnableFeignClients
@EnableHystrix
//@Import({org.springframework.cloud.openfeign.FeignClientsRegistrar.class})


//@EnableEurekaClient
@Inherited

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented

//载入RestTemplate 配置
@Import({
        RestTemplateConfig.class,
        ConsulRegisterConfig.class,
        FeignConfig.class,
        StreamConfig.class
})

//选配加载
//PromiseConfig.class
public @interface EnableApplicationClient {

}
