package com.example.auth.core.controller;

import com.github.microservice.auth.client.service.UserService;
import com.github.microservice.auth.security.annotations.ResourceAuth;
import com.github.microservice.auth.security.annotations.ResourceAuths;
import com.github.microservice.auth.security.helper.AuthHelper;
import com.github.microservice.auth.security.type.AuthType;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthHelper authHelper;

    @ApiOperation("刷新访问令牌")
    @GetMapping("refreshToken")
    public Object refreshToken(@ApiParam(name = "token", value = "刷新令牌", example = "e6uCWKIFmn0jBpYBLm-8J7H2I3g") String token) {
        return userService.refreshToken(token);
    }


    @PostMapping("test")
    @ResourceAuth("auth1")
    @ResourceAuth(value = "test1", remark = "测试权限1")
    @ResourceAuth("test2")
    @ResourceAuth(value = "superadmin", type = AuthType.Platform)
    @ApiOperation("测试接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uToken", paramType = "header", example = "xxx"),
            @ApiImplicitParam(name = "epId", paramType = "header", example = "xxx")
    })
    public Object test() {
        this.authHelper.log("time", System.currentTimeMillis());
        return new HashMap<>() {{
            put("user", authHelper.getCurrentUser());
            put("time", System.currentTimeMillis());
        }};
    }


    @PostMapping("test1")
    @ResourceAuth("test2")
    @ResourceAuth(value = "test1")
    public Object test1() {
        return new HashMap<>() {{
            put("time", System.currentTimeMillis());
        }};
    }

    @PostMapping("test2")
    @ResourceAuth("test4")
    public Object test2() {
        return new HashMap<>() {{
            put("time", System.currentTimeMillis());
        }};
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "uToken", paramType = "header", example = "xxx"),
    })
    @PostMapping("test3")
    public Object test3() {
        this.authHelper.log("demo", "111");
        this.authHelper.log("time", System.currentTimeMillis());
        return new HashMap<>() {{
            put("user", authHelper.getCurrentUser());
            put("time", System.currentTimeMillis());
        }};
    }

}
