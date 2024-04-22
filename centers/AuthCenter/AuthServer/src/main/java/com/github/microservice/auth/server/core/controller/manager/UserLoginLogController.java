package com.github.microservice.auth.server.core.controller.manager;


import com.github.microservice.auth.client.service.UserLoginLogService;
import com.github.microservice.core.delegate.DelegateMapping;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manager/user/login/log")
@DelegateMapping(types = UserLoginLogService.class)
public class UserLoginLogController implements UserLoginLogService {

    @Autowired
    @Delegate
    private UserLoginLogService userLoginLogService;
}
