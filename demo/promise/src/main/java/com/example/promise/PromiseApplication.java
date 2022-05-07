package com.example.promise;

import com.github.microservice.app.annotation.EnableApplicationClient;
import com.github.microservice.app.core.config.PromiseConfig;
import com.github.microservice.app.promise.annotation.Promise;
import com.github.microservice.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@EnableApplicationClient
@SpringBootApplication
@ComponentScan("com.example.promise.core")
@Import(PromiseConfig.class)
public class PromiseApplication extends ApplicationBootSuper {


    public static void main(String[] args) {
        SpringApplication.run(PromiseApplication.class, args);
    }

}
