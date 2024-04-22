package com.github.microservice.auth.client.service;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.model.AuthResourcesNameModel;
import com.github.microservice.auth.security.type.AuthType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "authserver/manager/authResourcesName")
public interface AuthResourcesNameService {

    @Operation(summary = "更新一个资源", description = "更新一个资源")
    @RequestMapping(value = "update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResultContent<Integer> update(@RequestBody AuthResourcesNameModel[] authResourcesNameModel);


    @Operation(summary = "分页查询所有资源", description = "分页查询所有资源")
    @RequestMapping(value = "list", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Page<AuthResourcesNameModel>> list(
            @Parameter(name = "authType", description = "权限类型", example = "Enterprise") @RequestParam("authType") AuthType authType,
            @Parameter(hidden = true) @PageableDefault Pageable pageable
    );
}
