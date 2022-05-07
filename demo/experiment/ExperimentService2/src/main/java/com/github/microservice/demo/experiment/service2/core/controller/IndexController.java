package com.github.microservice.demo.experiment.service2.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class IndexController {

    @RequestMapping("call")
    public Object call() {
        return Map.of("time", System.currentTimeMillis(),
                "me", "service2"
        );
    }
}
