package com.github.microservice.auth.server.core.controller.manager;


import com.github.microservice.auth.client.service.OrganizationUserService;
import com.github.microservice.core.delegate.DelegateMapping;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manager/organization/user")
@DelegateMapping(types = OrganizationUserService.class)
public class OrganizationUserManagerController implements OrganizationUserService {

    @Autowired
    @Delegate
    private OrganizationUserService organizationUserService;

}
