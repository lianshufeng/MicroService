package com.github.microservice.auth.client.service;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.example.ExampleConstant;
import com.github.microservice.auth.client.model.EnterpriseModel;
import com.github.microservice.auth.security.type.AuthType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

@FeignClient(name = "authserver/manager/enterprise")
public interface EnterpriseService {

    @ApiOperation(value = "企业是否存在", notes = "企业是否存在", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "exist", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    ResultContent<Void> existsByName(
            @ApiParam(name = "epId", value = "企业类型id", example = ExampleConstant.EnterpriseId) @RequestParam(value = "epId", required = false) String epId
    );


    @ApiOperation(value = "取企业信息", notes = "取企业信息", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "get", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    ResultContent<EnterpriseModel> get(
            @ApiParam(name = "epId", value = "企业类型id", example = ExampleConstant.EnterpriseId) @RequestParam(value = "epId", required = false) String epId
    );

    /**
     * 分页查询企业
     *
     * @param authType
     * @param pageable
     * @return
     */
    @ApiOperation(value = "查询企业列表", notes = "查询企业列表", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "list", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    ResultContent<EnterpriseModel> list(
            @ApiParam(name = "authType", value = "权限类型", example = "Enterprise") @RequestParam("authType") AuthType authType,
            @ApiIgnore @PageableDefault Pageable pageable);


    /**
     * 增加一个企业
     *
     * @return
     */
    @ApiOperation(value = "增加一个企业", notes = "增加一个企业", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResultContent<String> add(@RequestBody EnterpriseModel enterpriseModel);


    /**
     * 修改一个企业
     *
     * @param enterpriseModel
     * @return
     */
    @ApiOperation(value = "修改一个企业", notes = "增加一个企业", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResultContent<String> update(@RequestBody EnterpriseModel enterpriseModel);


    /**
     * 禁用企业
     *
     * @param epId
     * @return
     */
    @ApiOperation(value = "禁用企业", notes = "启用企业", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "disable", method = {RequestMethod.POST}, consumes = MediaType.ALL_VALUE)
    ResultContent<Void> disable(@ApiParam(name = "epId", value = "企业类型id", example = ExampleConstant.EnterpriseId) @RequestParam(value = "epId", required = false) String epId);


    /**
     * 启用企业
     *
     * @param epId
     * @return
     */
    @ApiOperation(value = "启用企业", notes = "启用企业", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "enable", method = {RequestMethod.POST}, consumes = MediaType.ALL_VALUE)
    ResultContent<Void> enable(@ApiParam(name = "epId", value = "企业类型id", example = ExampleConstant.EnterpriseId) @RequestParam(value = "epId", required = false) String epId);


}
