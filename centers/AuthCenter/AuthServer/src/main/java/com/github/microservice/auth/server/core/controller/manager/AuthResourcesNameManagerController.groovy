package com.github.microservice.auth.server.core.controller.manager

import com.github.microservice.auth.client.service.AuthResourcesNameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("manager/authResourcesName")
class AuthResourcesNameManagerController {

    @Autowired
    @Delegate(methodAnnotations = true, parameterAnnotations = true, includeTypes = AuthResourcesNameService.class, interfaces = false)
    private AuthResourcesNameService authResourcesNameService

}
