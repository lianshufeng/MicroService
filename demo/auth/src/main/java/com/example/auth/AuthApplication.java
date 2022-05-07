package com.example.auth;

import com.github.microservice.app.annotation.EnableApplicationClient;
import com.github.microservice.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableApplicationClient
@SpringBootApplication
@ComponentScan("com.example.auth.core")
public class AuthApplication extends ApplicationBootSuper {


    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
