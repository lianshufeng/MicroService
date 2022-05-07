package com.example.stream.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class StreamConfig {

    @Bean
    public Supplier<String> stringSupplier() {
        return () -> "Hello from Supplier";
    }

}
