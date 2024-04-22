package com.github.microservice.auth.server.core.controller.manager;

import com.github.microservice.auth.client.service.OrganizationService;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manager/organization")
public class OrganizationManagerController implements OrganizationService {

    @Autowired
    @Delegate
    private OrganizationService organizationService;

}
