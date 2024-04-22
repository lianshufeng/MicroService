package com.github.microservice.auth.client.service;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.example.ExampleConstant;
import com.github.microservice.auth.client.model.*;
import com.github.microservice.auth.client.type.DeviceType;
import com.github.microservice.auth.client.type.LoginType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "authserver/manager/user")
public interface UserService {


    @Operation(summary = "添加用户", description = "添加用户,必须含有")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    ResultContent<String> add(@RequestBody UserAuthModel userAuthModel);


    @Operation(summary = "获取用户信息",description = "获取用户信息")
    @RequestMapping(value = "get", method = {RequestMethod.POST})
    ResultContent<UserModel> get(
            @Parameter(name = "uid", description = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid
    );

    @Operation(
            summary = "更新用户的登录名",description = "修改用户的登陆名，手机，用户名，邮箱，身份证"
    )
    @RequestMapping(value = "updateLoginName", method = RequestMethod.POST)
    ResultContent<Void> updateLoginName(@RequestBody UserLoginModel userLoginModel);


    @Operation(summary = "添加令牌登陆",description = "添加令牌登陆方式，可用于短信验证、或别的登陆业务")
    @RequestMapping(value = "addTokenLogin", method = RequestMethod.POST)
    ResultContent<String> addTokenLogin(@RequestBody TokenLoginModel tokenLoginModel);


    @Operation(summary = "删除令牌登陆",description = "删除令牌登陆")
    @RequestMapping(value = "removeTokenLogin", method = RequestMethod.POST)
    ResultContent<Void> removeTokenLogin(
            @Parameter(name = "token", description = "登陆令牌", example = "0bcd35cc75424639b63b72b9946572ba") @RequestParam("token") String token
    );


    @Operation(summary = "用户登录",description = "用户登陆，必须包含登陆类型和登陆值")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    ResultContent<LoginTokenModel> login(@RequestBody UserAuthLoginModel userAuthLoginModel);


    @Operation(summary = "查询用户信息",description = "通过登陆方式查询用户")
    @RequestMapping(value = "queryFromLoginType", method = {RequestMethod.POST})
    ResultContent<UserModel> queryFromLoginType(
            @Parameter(name = "loginType", description = "登陆类型", example = "Phone") @RequestParam("loginType") LoginType loginType,
            @Parameter(name = "loginValue", description = "登陆值", example = "15123241353") @RequestParam("loginValue") String loginValue
    );

    @Operation(summary = "更新用户登录密码",description = "更新用户登录密码")
    @RequestMapping(value = "updateLoginPassword", method = {RequestMethod.POST})
    ResultContent<Void> updateLoginPassword(
            @Parameter(name = "uid", description = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid,
            @Parameter(name = "passWord", description = "新密码", example = "123456") @RequestParam("passWord") String passWord
    );

    @Operation(summary = "校验用户密码",description = "更新用户登录密码")
    @RequestMapping(value = "checkLoginPassword", method = {RequestMethod.POST})
    ResultContent<Void> checkLoginPassword(
            @Parameter(name = "uid", description = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid,
            @Parameter(name = "passWord", description = "新密码", example = "123456") @RequestParam("passWord") String passWord
    );


    @Operation(summary = "查询令牌",description = "查询用户的访问令牌")
    @RequestMapping(value = "queryToken", method = {RequestMethod.POST})
    ResultContent<UserTokenModel> queryToken(
            @Parameter(name = "accessToken", description = "访问令牌", example = "xxxx") @RequestParam(value = "accessToken") String accessToken
    );


    @Operation(summary = "刷新令牌",description = "刷新用户访问令牌")
    @RequestMapping(value = "refreshToken", method = {RequestMethod.POST})
    ResultContent<UserTokenModel> refreshToken(
            @Parameter(name = "refreshToken", description = "刷新令牌", example = "ShkUxSATO2SNrQ7CLixCHTwA354") @RequestParam("refreshToken") String refreshToken
    );


    /**
     * 用户id注销
     *
     * @param deviceType
     * @param uid
     * @return
     */
    @Operation(summary = "用户id注销",description = "通过用户id进行注销，如果设备类型为null,则注销该用户uid的所有token")
    @RequestMapping(value = "logoutFromUid", method = {RequestMethod.POST})
    ResultContent<Long> logoutFromUid(
            @Parameter(name = "deviceType", description = "设备类型,如果为空则删除所有用户下的令牌", example = "Pc") @RequestParam(value = "deviceType", required = false) DeviceType deviceType,
            @Parameter(name = "uid", description = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid
    );


    /**
     * 用户令牌注销
     *
     * @param accessToken
     * @return
     */
    @Operation(summary = "用户令牌注销",description = "用户通过访问令牌进行注销")
    @RequestMapping(value = "logoutFromToken", method = {RequestMethod.POST})
    ResultContent<Long> logoutFromToken(
            @Parameter(name = "accessToken", description = "访问令牌", example = "DsUM2xHrDt1rthlDPCQVueWiOwo") @RequestParam("accessToken") String accessToken
    );


    /**
     * 禁用用户
     *
     * @return
     */
    @Operation(summary = "禁用用户",description = "禁用用户id，并注销该用户的所有令牌")
    @RequestMapping(value = "disable", method = {RequestMethod.POST})
    ResultContent<Void> disable(
            @Parameter(name = "uid", description = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid
    );


    /**
     * 禁用用户
     *
     * @return
     */
    @Operation(summary = "启用用户",description = "启用用户id")
    @RequestMapping(value = "enable", method = {RequestMethod.POST})
    ResultContent<Void> enable(
            @Parameter(name = "uid", description = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid
    );


//    unregister

    @Operation(summary = "账户销户",description = "账户销户")
    @RequestMapping(value = "unRegister", method = {RequestMethod.POST})
    ResultContent<Void> unRegister(
            @Parameter(name = "uid", description = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid,
            @Parameter(name = "passWord", description = "密码", example = "123456") @RequestParam("passWord") String passWord
    );


}


