package com.example.feigncall.core.controller;

import com.example.feignclient.model.User;
import com.example.feignclient.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping("put")
    @HystrixCommand(fallbackMethod = "putError")
    public Object put(User user) {
        return this.userService.put(user);
    }


    public Object putError(User user) {
        System.out.println("putError-->");
        return null;
    }


    @RequestMapping("put2")
    public Object put2(User user) {
        return this.restTemplate.postForEntity("http://feignserver/user/put", user, Map.class);
    }

}
