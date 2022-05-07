package com.example.stream.core.controller;

import com.github.microservice.app.stream.StreamHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class StreamController {
    private final static String StreamName = "test-stream";


    @Autowired
    private StreamHelper streamHelper;


    @RequestMapping("send")
    public Object send(@RequestBody String body) {
        log.info("send -> {} -> {}", StreamName, body);
        return this.streamHelper.send(StreamName, body);
    }


}
