package com.github.microservice.auth.server.core.controller.manager;

import com.github.microservice.auth.client.service.RoleService;
import com.github.microservice.core.delegate.DelegateMapping;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manager/role")
@DelegateMapping(types = RoleService.class)
public class RoleManagerController implements RoleService {

    @Autowired
    @Delegate
    private RoleService roleService;

}
