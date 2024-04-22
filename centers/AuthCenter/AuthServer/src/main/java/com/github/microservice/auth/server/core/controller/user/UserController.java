package com.github.microservice.auth.server.core.controller.user;

import com.github.microservice.auth.client.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "刷新令牌", description = "刷新用户访问令牌")
    @RequestMapping(value = "refreshToken", method = {RequestMethod.GET, RequestMethod.POST})
    public Object refreshToken(@Parameter(name = "refreshToken", description = "刷新令牌", example = "ShkUxSATO2SNrQ7CLixCHTwA354") @RequestParam("refreshToken") String refreshToken) {
        return this.userService.refreshToken(refreshToken);
    }


}
