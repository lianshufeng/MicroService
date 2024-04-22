package com.github.microservice.auth.server.core.controller.manager;

import com.github.microservice.auth.client.service.AuthResourcesNameService;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manager/authResourcesName")
public class AuthResourcesNameManagerController implements AuthResourcesNameService {

    @Autowired
    @Delegate
    private AuthResourcesNameService authResourcesNameService;


}
