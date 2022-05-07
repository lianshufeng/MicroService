package com.github.microservice.app.stream;

import com.github.microservice.app.stream.message.MessageBusHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StreamConfig {

    @Bean
    public StreamHelper streamHelper() {
        return new StreamHelper();
    }

    @Bean
    public MessageBusHelper messageBusHelper() {
        return new MessageBusHelper();
    }

}
