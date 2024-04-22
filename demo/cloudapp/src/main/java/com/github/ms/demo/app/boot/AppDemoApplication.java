package com.github.ms.demo.app.boot;

import com.github.microservice.app.annotation.EnableApplicationClient;
import com.github.microservice.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan("com.github.ms.demo.app.core")
@EnableApplicationClient
public class AppDemoApplication extends ApplicationBootSuper {

    public static void main(String[] args) {
        SpringApplication.run(AppDemoApplication.class, args);
    }

}
