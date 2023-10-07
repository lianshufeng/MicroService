package com.example.promise.core.controller;

import com.example.promise.core.custom.UserCustomPromise;
import com.github.microservice.app.promise.annotation.Promise;
import com.github.microservice.app.stream.StreamHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
@RestController
@RequestMapping
public class TestController {

    private static AtomicLong tryCount = new AtomicLong();

    @Autowired
    private StreamHelper streamHelper;


    @RequestMapping("userCustomPromise")
    public void userCustomPromise() {
        streamHelper.send(UserCustomPromise.Name, UserCustomPromise.Message.builder().time(System.currentTimeMillis()).build());
    }


    //    @HystrixCommand(fallbackMethod = "userPromise")
    @RequestMapping("user")
    @Promise("userPromise")
    public Object user(String name, Integer age) {
        int a = 1 / 0;
        return new HashMap<>() {{
            put("time", System.currentTimeMillis());
        }};
    }

    public Object userPromise(String name, Integer age) {
        log.info("try count ->  : {} , {} , {}", tryCount.addAndGet(1), name, age);
        int a = 1 / 0;
        System.out.println("补偿结束...");
        return null;
    }

}
