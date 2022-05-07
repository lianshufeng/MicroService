package com.example.stream.core.stream;

import com.github.microservice.app.stream.StreamConsumer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("consumer")
public class TestStream extends StreamConsumer<Map<String, Object>> {

    @Override
    public void accept(Map<String, Object> s) {
        System.out.println("rev : " + s);
    }
}
