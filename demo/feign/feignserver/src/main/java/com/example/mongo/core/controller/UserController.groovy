package com.example.mongo.core.controller

import com.example.feignclient.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController {

    @Autowired
    @Delegate(methodAnnotations = true, parameterAnnotations = true, includeTypes = UserService.class, interfaces = false)
    private UserService userService;

}
