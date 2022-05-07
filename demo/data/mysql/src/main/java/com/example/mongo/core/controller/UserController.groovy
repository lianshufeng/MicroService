package com.example.mongo.core.controller

import com.example.mongo.core.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @Autowired
    @Delegate(methodAnnotations = true, parameterAnnotations = true, includeTypes = UserService.class, interfaces = false)
    private UserService userService;
}

