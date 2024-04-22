package com.example.auth.core.controller;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.service.UserService;
import com.github.microservice.auth.security.annotations.ResourceAuth;
import com.github.microservice.auth.security.helper.AuthHelper;
import com.github.microservice.auth.security.model.AuthDetails;
import com.github.microservice.auth.security.type.AuthType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthHelper authHelper;

    //    @ApiOperation("刷新访问令牌")
    @GetMapping("refreshToken")
    public Object refreshToken(
            @Parameter(name = "token", description = "刷新令牌", example = "") @RequestParam("token") String token
    ) {
        return userService.refreshToken(token);
    }


    //    @ApiOperation("get")
    @GetMapping("get")
    @ResourceAuth(value = "user", type = AuthType.User)
    public ResultContent<Object> get() {
        AuthDetails authDetails = authHelper.getCurrentUser();
        System.out.println(authDetails);
        return ResultContent.buildContent(authDetails);
    }

    @PostMapping("test")
    @ResourceAuth("auth1")
    @ResourceAuth(value = "test1", remark = "测试权限1")
    @ResourceAuth("test2")
    @ResourceAuth(value = "superadmin", type = AuthType.Platform)
//    @ApiOperation("测试接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "uToken", paramType = "header", example = "xxx"),
//            @ApiImplicitParam(name = "epId", paramType = "header", example = "xxx")
//    })
    @Operation(summary = "test", description = "测试")
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
        this.authHelper.log("test1", System.currentTimeMillis());
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


    //    @ApiImplicitParams({
//            @ApiImplicitParam(name = "uToken", paramType = "header", example = "xxx"),
//    })
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
