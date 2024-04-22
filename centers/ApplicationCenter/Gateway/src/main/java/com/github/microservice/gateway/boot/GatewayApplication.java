package com.github.microservice.gateway.boot;

import com.github.microservice.app.annotation.EnableApplicationClient;
import com.github.microservice.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;


@EnableApplicationClient
@ComponentScan("com.github.microservice.gateway.core")
public class GatewayApplication extends ApplicationBootSuper {


    /**
     * 默认入口方法
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }


}
