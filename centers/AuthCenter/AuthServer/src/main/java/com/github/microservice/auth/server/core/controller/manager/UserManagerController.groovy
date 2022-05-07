package com.github.microservice.auth.server.core.controller.manager

import com.github.microservice.auth.client.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("manager/user")
class UserManagerController {

    @Autowired
    @Delegate(methodAnnotations = true, parameterAnnotations = true, includeTypes = UserService.class, interfaces = false)
    private UserService userService

}
