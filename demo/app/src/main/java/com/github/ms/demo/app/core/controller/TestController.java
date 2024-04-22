package com.github.ms.demo.app.core.controller;

import com.github.ms.demo.app.core.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("time")
    public Object time() {
        return Map.of("time", testService.time());
    }


}
