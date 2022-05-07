package com.example.stream;

import com.github.microservice.app.annotation.EnableApplicationClient;
import com.github.microservice.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableApplicationClient
@SpringBootApplication
@ComponentScan("com.example.stream.core")
public class StreamApplication extends ApplicationBootSuper {


    public static void main(String[] args) {
        SpringApplication.run(StreamApplication.class, args);
    }

}
