package com.github.microservice.auth.server.core.controller.manager


import com.github.microservice.auth.client.service.EnterpriseUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("manager/enterprise/user")
class EnterpriseUserManagerController {

    @Autowired
    @Delegate(methodAnnotations = true, parameterAnnotations = true, includeTypes = EnterpriseUserService.class, interfaces = false)
    private EnterpriseUserService enterpriseUserService

}
