package com.github.microservice.auth.server.core.controller.manager

import com.github.microservice.auth.client.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("manager/role")
class RoleManagerController {

    @Autowired
    @Delegate(methodAnnotations = true, parameterAnnotations = true, includeTypes = RoleService.class, interfaces = false)
    private RoleService roleService

}
