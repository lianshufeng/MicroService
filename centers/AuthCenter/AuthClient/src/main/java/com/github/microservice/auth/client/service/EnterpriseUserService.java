package com.github.microservice.auth.client.service;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.example.ExampleConstant;
import com.github.microservice.auth.client.model.EnterpriseUserModel;
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

@FeignClient(name = "authserver/manager/enterprise/user")
public interface EnterpriseUserService {


    /**
     * 条件查询企业里的用户
     *
     * @param mql
     * @param fields
     * @param pageable
     * @return
     */
    @ApiOperation(value = "条件查询企业里的用户", notes = "条件查询企业里的用户", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "query", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Page<EnterpriseUserModel>> query(
            @ApiParam(name = "mql", value = "mongo查询语法", example = "{ 'enterprise' : { '$ref':'enterprise' , '$id': ObjectId('" + ExampleConstant.EnterpriseId + "') } }") @RequestParam("mql") String mql,
            @ApiParam(name = "fields", value = "过滤显示的字段，null为全部显示", example = "[id]", required = false) @RequestParam(value = "fields", required = false) String[] fields,
            @ApiIgnore @PageableDefault Pageable pageable);


    @ApiOperation(value = "查询一个企业里用户的信息", notes = "查询一个企业里用户的信息", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "get", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<EnterpriseUserModel> get(
            @ApiParam(name = "enterpriseId", value = "访问令牌", example = ExampleConstant.EnterpriseId) @RequestParam("enterpriseId") String enterpriseId,
            @ApiParam(name = "uid", value = "访问令牌", example = ExampleConstant.UserId) @RequestParam("uid") String uid
    );


    @ApiOperation(value = "查询一个用户所在的所有企业", notes = "查询一个用户所在的所有企业", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "listEnterprise", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Page<String>> listEnterprise(
            @ApiParam(name = "uid", value = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid,
            @ApiIgnore @PageableDefault Pageable pageable
    );


    @ApiOperation(value = "查询用户的附属企业", notes = "查询一个用户所在的附属企业，在企业中至少有一个角色", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "affiliatesEnterprise", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Page<String>> affiliatesEnterprise(
            @ApiParam(name = "uid", value = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid,
            @ApiIgnore @PageableDefault Pageable pageable
    );

    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "update", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Void> update(
            @ApiParam(name = "enterpriseId", value = "过滤显示的字段，null为全部显示", example = ExampleConstant.EnterpriseId) @RequestParam("enterpriseId") String enterpriseId,
            @ApiParam(name = "uid", value = "过滤显示的字段，null为全部显示", example = "[" + ExampleConstant.UserId + "]") @RequestParam("uid") String[] uid);

}
