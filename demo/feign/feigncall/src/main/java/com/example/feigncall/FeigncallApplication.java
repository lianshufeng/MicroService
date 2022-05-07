package com.example.feigncall;

import com.github.microservice.app.annotation.EnableApplicationClient;
import com.github.microservice.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableApplicationClient
@SpringBootApplication
@ComponentScan("com.example.feigncall.core")
public class FeigncallApplication extends ApplicationBootSuper {


    public static void main(String[] args) {
        SpringApplication.run(FeigncallApplication.class, args);
    }

}
