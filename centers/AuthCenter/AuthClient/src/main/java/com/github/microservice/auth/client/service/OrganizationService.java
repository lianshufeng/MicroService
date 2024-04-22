package com.github.microservice.auth.client.service;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.example.ExampleConstant;
import com.github.microservice.auth.client.model.OrganizationModel;
import com.github.microservice.auth.security.type.AuthType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "authserver/manager/organization")
public interface OrganizationService {

    @Operation(summary = "机构是否存在", description = "机构是否存在")
    @RequestMapping(value = "exist", method = RequestMethod.POST)
    ResultContent<Void> existsByName(@Parameter(name = "name", description = "机构名", example = "user") @RequestParam(value = "name", required = false) String name);


    @Operation(summary = "取机构信息", description = "取机构信息")
    @RequestMapping(value = "get", method = RequestMethod.POST)
    ResultContent<OrganizationModel> get(@Parameter(name = "oid", description = "机构类型id", example = ExampleConstant.OrganizationId) @RequestParam(value = "oid", required = false) String oid);

    @Operation(summary = "通过机构名查询", description = "通过机构名查询")
    @RequestMapping(value = "findByName", method = RequestMethod.POST)
    ResultContent<OrganizationModel> findByName(@Parameter(name = "name", description = "机构名", example = "name") @RequestParam(value = "name", required = false) String name);

    /**
     * 分页查询企业
     *
     * @param authType
     * @param pageable
     * @return
     */
    @Operation(summary = "查询机构列表", description = "查询机构列表")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    ResultContent<OrganizationModel> list(@Parameter(name = "authType", description = "权限类型", example = "Enterprise") @RequestParam("authType") AuthType authType, @Parameter(hidden = true) @PageableDefault Pageable pageable);


    /**
     * 增加一个机构
     *
     * @return
     */
    @Operation(summary = "增加一个机构", description = "增加一个机构")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    ResultContent<String> add(@RequestBody OrganizationModel organizationModel);


    /**
     *
     * @param organizationModel
     * @return
     */
    @Operation(summary = "修改一个机构", description = "修改一个机构")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    ResultContent<String> update(@RequestBody OrganizationModel organizationModel);


    /**
     *
     * @param oid
     * @return
     */
    @Operation(summary = "禁用机构", description = "禁用机构")
    @RequestMapping(value = "disable", method = {RequestMethod.POST})
    ResultContent<Void> disable(@Parameter(name = "oid", description = "机构类型id", example = ExampleConstant.OrganizationId) @RequestParam(value = "oid", required = false) String oid);


    /**
     *
     * @param oid
     * @return
     */
    @Operation(summary = "启用机构", description = "启用机构")
    @RequestMapping(value = "enable", method = {RequestMethod.POST})
    ResultContent<Void> enable(@Parameter(name = "oid", description = "机构类型id", example = ExampleConstant.OrganizationId) @RequestParam(value = "oid", required = false) String oid);


}
