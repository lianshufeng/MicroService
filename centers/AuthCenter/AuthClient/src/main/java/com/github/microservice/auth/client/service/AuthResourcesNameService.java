package com.github.microservice.auth.client.service;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.example.ExampleConstant;
import com.github.microservice.auth.client.model.AuthResourcesNameModel;
import com.github.microservice.auth.security.type.AuthType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient(name = "authserver/manager/authResourcesName")
public interface AuthResourcesNameService {

    @ApiOperation(value = "更新一个资源", notes = "更新一个资源", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResultContent<Integer> update(@RequestBody AuthResourcesNameModel[] authResourcesNameModel);


    @ApiOperation(value = "分页查询所有资源", notes = "分页查询所有资源", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "list", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Page<AuthResourcesNameModel>> list(
            @ApiParam(name = "authType", value = "权限类型", example = "Enterprise") @RequestParam("authType") AuthType authType,
            @ApiIgnore @PageableDefault Pageable pageable
    );
}
