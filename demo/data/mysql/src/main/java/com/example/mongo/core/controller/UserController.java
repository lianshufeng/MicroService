package com.example.mongo.core.controller;

import com.example.mongo.core.service.UserService;
import com.github.microservice.core.delegate.DelegateMapping;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@DelegateMapping(types = UserService.class)
public class UserController {

    @Autowired
    @Delegate
    private UserServiceImpl userService;

    @RequestMapping(value = "find", method = RequestMethod.GET)
    public Object find(@RequestParam(value = "name") String name) {
        return this.userService.find(name);
    }

    @RequestMapping(value = "findByName", method = RequestMethod.GET)
    public Object findByName(@RequestParam(value = "name") String name) {
        return this.userService.findByName(name);
    }


}

