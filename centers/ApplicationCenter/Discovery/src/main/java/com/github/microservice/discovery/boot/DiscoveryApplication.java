package com.github.microservice.discovery.boot;

import com.github.microservice.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan("com.github.microservice.discovery.core")
public class DiscoveryApplication extends ApplicationBootSuper {


    /**
     * 默认入口方法
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryApplication.class, args);
    }


}
