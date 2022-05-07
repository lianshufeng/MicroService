package com.github.microservice.auth.server.core.controller.user;

import com.github.microservice.auth.client.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "刷新令牌", notes = "刷新用户访问令牌", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "refreshToken", method = {RequestMethod.GET, RequestMethod.POST})
    public Object refreshToken(@ApiParam(name = "refreshToken", value = "刷新令牌", example = "ShkUxSATO2SNrQ7CLixCHTwA354") @RequestParam("refreshToken") String refreshToken) {
        return this.userService.refreshToken(refreshToken);
    }


}
