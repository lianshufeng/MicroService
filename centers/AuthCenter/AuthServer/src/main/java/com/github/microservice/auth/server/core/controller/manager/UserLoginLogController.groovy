package com.github.microservice.auth.server.core.controller.manager


import com.github.microservice.auth.client.service.UserLoginLogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("manager/user/login/log")
class UserLoginLogController {

    @Autowired
    @Delegate(methodAnnotations = true, parameterAnnotations = true, includeTypes = UserLoginLogService.class, interfaces = false)
    private UserLoginLogService userLoginLogService
}
