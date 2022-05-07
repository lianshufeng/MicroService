package com.github.microservice.auth.client.service;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.example.ExampleConstant;
import com.github.microservice.auth.client.model.*;
import com.github.microservice.auth.client.type.DeviceType;
import com.github.microservice.auth.client.type.LoginType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "authserver/manager/user")
public interface UserService {


    @ApiOperation(value = "添加用户", notes = "添加用户,必须含有", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResultContent<String> add(@RequestBody UserAuthModel userAuthModel);


    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "get", method = {RequestMethod.POST}, consumes = MediaType.ALL_VALUE)
    ResultContent<UserModel> get(
            @ApiParam(name = "uid", value = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid
    );

    @ApiOperation(
            value = "更新用户的登录名", notes = "修改用户的登陆名，手机，用户名，邮箱，身份证",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @RequestMapping(value = "updateLoginName", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResultContent<Void> updateLoginName(@RequestBody UserLoginModel userLoginModel);


    @ApiOperation(value = "添加令牌登陆", notes = "添加令牌登陆方式，可用于短信验证、或别的登陆业务", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "addTokenLogin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResultContent<String> addTokenLogin(@RequestBody TokenLoginModel tokenLoginModel);


    @ApiOperation(value = "删除令牌登陆", notes = "删除令牌登陆", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "removeTokenLogin", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    ResultContent<Void> removeTokenLogin(
            @ApiParam(name = "token", value = "登陆令牌", example = "0bcd35cc75424639b63b72b9946572ba") @RequestParam("token") String token
    );


    @ApiOperation(value = "用户登录", notes = "用户登陆，必须包含登陆类型和登陆值", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResultContent<LoginTokenModel> login(@RequestBody UserAuthLoginModel userAuthLoginModel);


    @ApiOperation(value = "查询用户信息", notes = "通过登陆方式查询用户", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "queryFromLoginType", method = {RequestMethod.POST}, consumes = MediaType.ALL_VALUE)
    ResultContent<UserModel> queryFromLoginType(
            @ApiParam(name = "loginType", value = "登陆类型", example = "Phone") @RequestParam("loginType") LoginType loginType,
            @ApiParam(name = "loginValue", value = "登陆值", example = "15123241353") @RequestParam("loginValue") String loginValue
    );

    @ApiOperation(value = "更新用户登录密码", notes = "更新用户登录密码", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "updateLoginPassword", method = {RequestMethod.POST}, consumes = MediaType.ALL_VALUE)
    ResultContent<Void> updateLoginPassword(
            @ApiParam(name = "uid", value = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid,
            @ApiParam(name = "passWord", value = "新密码", example = "123456") @RequestParam("passWord") String passWord
    );

    @ApiOperation(value = "校验用户密码", notes = "更新用户登录密码", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "checkLoginPassword", method = {RequestMethod.POST}, consumes = MediaType.ALL_VALUE)
    ResultContent<Void> checkLoginPassword(
            @ApiParam(name = "uid", value = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid,
            @ApiParam(name = "passWord", value = "新密码", example = "123456") @RequestParam("passWord") String passWord
    );


    @ApiOperation(value = "查询令牌", notes = "查询用户的访问令牌", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "queryToken", method = {RequestMethod.POST}, consumes = MediaType.ALL_VALUE)
    ResultContent<UserTokenModel> queryToken(
            @ApiParam(name = "accessToken", value = "访问令牌", example = "xxxx") @RequestParam("accessToken") String accessToken
    );


    @ApiOperation(value = "刷新令牌", notes = "刷新用户访问令牌", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "refreshToken", method = {RequestMethod.POST}, consumes = MediaType.ALL_VALUE)
    ResultContent<UserTokenModel> refreshToken(
            @ApiParam(name = "refreshToken", value = "刷新令牌", example = "ShkUxSATO2SNrQ7CLixCHTwA354") @RequestParam("refreshToken") String refreshToken
    );


    /**
     * 用户id注销
     *
     * @param deviceType
     * @param uid
     * @return
     */
    @ApiOperation(value = "用户id注销", notes = "通过用户id进行注销，如果设备类型为null,则注销该用户uid的所有token", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "logoutFromUid", method = {RequestMethod.POST}, consumes = MediaType.ALL_VALUE)
    ResultContent<Long> logoutFromUid(
            @ApiParam(name = "deviceType", value = "设备类型,如果为空则删除所有用户下的令牌", example = "Pc") @RequestParam(value = "deviceType", required = false) DeviceType deviceType,
            @ApiParam(name = "uid", value = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid
    );


    /**
     * 用户令牌注销
     *
     * @param accessToken
     * @return
     */
    @ApiOperation(value = "用户令牌注销", notes = "用户通过访问令牌进行注销", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "logoutFromToken", method = {RequestMethod.POST}, consumes = MediaType.ALL_VALUE)
    ResultContent<Long> logoutFromToken(
            @ApiParam(name = "accessToken", value = "访问令牌", example = "DsUM2xHrDt1rthlDPCQVueWiOwo") @RequestParam("accessToken") String accessToken
    );


    /**
     * 禁用用户
     *
     * @return
     */
    @ApiOperation(value = "禁用用户", notes = "禁用用户id，并注销该用户的所有令牌", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "disable", method = {RequestMethod.POST}, consumes = MediaType.ALL_VALUE)
    ResultContent<Void> disable(
            @ApiParam(name = "uid", value = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid
    );


    /**
     * 禁用用户
     *
     * @return
     */
    @ApiOperation(value = "启用用户", notes = "启用用户id", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "enable", method = {RequestMethod.POST}, consumes = MediaType.ALL_VALUE)
    ResultContent<Void> enable(
            @ApiParam(name = "uid", value = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid
    );


}


