package com.github.microservice.auth.server;

import com.github.microservice.app.annotation.EnableApplicationClient;
import com.github.microservice.core.boot.ApplicationBootSuper;
import com.github.microservice.core.util.scan.ClassScanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableApplicationClient
@ComponentScan("com.github.microservice.auth.server.core")
public class AuthServerApplication extends ApplicationBootSuper {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

}
