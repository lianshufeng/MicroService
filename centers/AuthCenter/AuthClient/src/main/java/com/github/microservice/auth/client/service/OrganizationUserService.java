package com.github.microservice.auth.client.service;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.example.ExampleConstant;
import com.github.microservice.auth.client.model.OrganizationUserModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "authserver/manager/organization/user")
public interface OrganizationUserService {


    /**
     * 条件查询企业里的用户
     *
     * @param mql
     * @param fields
     * @param pageable
     * @return
     */
    @Operation(summary = "条件查询机构里的用户", description = "条件查询机构里的用户")
    @RequestMapping(value = "query", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Page<OrganizationUserModel>> query(
            @Parameter(name = "mql", description = "mongo查询语法", example = "{ 'organization' : { '$ref':'organization' , '$id': ObjectId('" + ExampleConstant.OrganizationId + "') } }") @RequestParam("mql") String mql,
            @Parameter(name = "fields", description = "过滤显示的字段，null为全部显示", example = "[\"user\"]", required = false) @RequestParam(value = "fields", required = false) String[] fields,
            @Parameter(hidden = true) @PageableDefault Pageable pageable);


    @Operation(summary = "查询一个机构里用户的信息", description = "查询一个机构里用户的信息")
    @RequestMapping(value = "get", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<OrganizationUserModel> get(
            @Parameter(name = "organizationId", description = "访问令牌", example = ExampleConstant.OrganizationId) @RequestParam("organizationId") String organizationId,
            @Parameter(name = "uid", description = "访问令牌", example = ExampleConstant.UserId) @RequestParam("uid") String uid
    );


    @Operation(summary = "查询一个用户所在的所有机构", description = "查询一个用户所在的所有机构")
    @RequestMapping(value = "listOrganization", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Page<String>> listOrganization(
            @Parameter(name = "uid", description = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid,
            @Parameter(hidden = true) @PageableDefault Pageable pageable
    );


    @Operation(summary = "查询用户的附属机构", description = "查询一个用户所在的附属机构，在机构中至少有一个角色")
    @RequestMapping(value = "affiliatesOrganization", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Page<String>> affiliatesOrganization(
            @Parameter(name = "uid", description = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid,
            @Parameter(hidden = true) @PageableDefault Pageable pageable
    );

    @Operation(summary = "更新用户信息", description = "更新用户信息")
    @RequestMapping(value = "update", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Void> update(
            @Parameter(name = "organizationId", description = "过滤显示的字段，null为全部显示", example = ExampleConstant.OrganizationId) @RequestParam("organizationId") String organizationId,
            @Parameter(name = "uid", description = "过滤显示的字段，null为全部显示", example = "[" + ExampleConstant.UserId + "]") @RequestParam("uid") String[] uid);

}
