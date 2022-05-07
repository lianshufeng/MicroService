package com.github.microservice.auth.client.service;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.example.ExampleConstant;
import com.github.microservice.auth.client.model.UserLoginLogModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

@FeignClient(name = "authserver/manager/user/login/log")
public interface UserLoginLogService {


    /**
     * 查询用户的登陆记录
     *
     * @param uid
     * @param pageable
     * @return
     */
    @ApiOperation(value = "分页查询用户的登录记录", notes = "分页查询企业下的角色", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "list", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Page<UserLoginLogModel>> list(
            @ApiParam(name = "uid", value = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid,
            @ApiIgnore @PageableDefault(sort = {"createTime,desc"}) Pageable pageable);


}
