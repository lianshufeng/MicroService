package com.example.mongo;

import com.github.microservice.app.annotation.EnableApplicationClient;
import com.github.microservice.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableApplicationClient
@ComponentScan("com.example.mongo.core")
public class MongoApplication extends ApplicationBootSuper {

    public static void main(String[] args) {
        SpringApplication.run(MongoApplication.class, args);
    }

}
