package com.github.microservice.auth.client.service;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.example.ExampleConstant;
import com.github.microservice.auth.client.model.RoleGroupModel;
import com.github.microservice.auth.client.model.RoleModel;
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
import java.util.Set;

@FeignClient(name = "authserver/manager/role")
public interface RoleService {


    //------------ 角色 ---------------------
    @ApiOperation(value = "更新角色", notes = "更新角色，有id则更新,无id则新增,注:仅更新非null对象", consumes = "application/json")
    @RequestMapping(value = "updateRole", method = RequestMethod.POST)
    ResultContent<String> updateRole(@RequestBody RoleModel roleModel);

    @ApiOperation(value = "删除一个角色", notes = "删除一个角色", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "removeRole", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Void> removeRole(
            @ApiParam(name = "roleId", value = "角色id", example = ExampleConstant.RoleId) @RequestParam("roleId") String roleId
    );

    @ApiOperation(value = "查询一个角色", notes = "查询角色", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "getRole", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<RoleModel> getRole(
            @ApiParam(name = "roleId", value = "角色id", example = ExampleConstant.RoleId) @RequestParam("roleId") String roleId
    );

    //--------------- 角色组 -------------------
    @ApiOperation(value = "更新角色组", notes = "更新角色组", consumes = "application/json")
    @RequestMapping(value = "updateRoleGroup", method = RequestMethod.POST)
    ResultContent<String> updateRoleGroup(@RequestBody RoleGroupModel roleGroupModel);

    @ApiOperation(value = "删除一个角色组", notes = "删除一个角色组", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "removeRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Void> removeRoleGroup(
            @ApiParam(name = "roleGroupId", value = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId
    );

    @ApiOperation(value = "查询一个角色组", notes = "查询角色", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "getRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<RoleGroupModel> getRoleGroup(
            @ApiParam(name = "roleGroupId", value = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId
    );


    // --------------角色组企业的关系

    @ApiOperation(value = "查询企业中的角色组", notes = "查询企业中的角色组", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "listRoleGroupFromEnterprise", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<List<RoleGroupModel>> listRoleGroupFromEnterprise(
            @ApiParam(name = "enterpriseId", value = "企业id", example = ExampleConstant.EnterpriseId) @RequestParam("enterpriseId") String enterpriseId
    );


    @ApiOperation(value = "通过身份查询角色列表", notes = "通过身份查询角色列表", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "findByIdentity", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<List<RoleGroupModel>> findByIdentity(
            @ApiParam(name = "enterpriseId", value = "企业id", example = ExampleConstant.EnterpriseId) @RequestParam("enterpriseId") String enterpriseId,
            @ApiParam(name = "identity", value = "身份名", example = "[" + ExampleConstant.Identity + "]") @RequestParam("identity") String[] identity);


    //------------------ 角色组关系 --------------------------------
    @ApiOperation(value = "在角色组中添加角色", notes = "在角色组中添加角色", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "addRoleToRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Void> addRoleToRoleGroup(
            @ApiParam(name = "roleGroupId", value = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId,
            @ApiParam(name = "roleId", value = "角色id", example = ExampleConstant.RoleId) @RequestParam("roleId") String[] roleId);


    @ApiOperation(value = "删除角色组中的角色", notes = "删除角色组中的角色", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "removeRoleFromRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Void> removeRoleFromRoleGroup(
            @ApiParam(name = "roleGroupId", value = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId,
            @ApiParam(name = "roleId", value = "角色id", example = ExampleConstant.RoleId) @RequestParam("roleId") String[] roleId);


    @ApiOperation(value = "查询角色所在的角色组", notes = "查询角色所在的角色组", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "listRoleGroupFromRole", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<List<RoleGroupModel>> listRoleGroupFromRole(
            @ApiParam(name = "roleId", value = "角色id", example = ExampleConstant.RoleId) @RequestParam("roleId") String[] roleId
    );


    //------------------------ 角色组里添加用户 -------------------------------------------------

    @ApiOperation(value = "在角色组中添加用户", notes = "在角色组中添加用户", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "addUserToRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Set<String>> addUserToRoleGroup(
            @ApiParam(name = "roleGroupId", value = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId,
            @ApiParam(name = "uid", value = "用户id", example = "[" + ExampleConstant.UserId + "]") @RequestParam("uid") String[] uid);

    @ApiOperation(value = "为用户添加多个角色组", notes = "在角色组中添加用户", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "addUsersToRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Set<String>> addUsersToRoleGroup(
            @ApiParam(name = "uid", value = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid,
            @ApiParam(name = "roleGroupIds", value = "角色组id", example = "[" + ExampleConstant.RoleGroupId + "]" ) @RequestParam("roleGroupIds") String[] roleGroupIds
    );


    @ApiOperation(value = "在角色组中删除用户", notes = "在角色组中删除用户", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "removeUserFromRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Set<String>> removeUserFromRoleGroup(
            @ApiParam(name = "roleGroupId", value = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId,
            @ApiParam(name = "uid", value = "用户id", example = "[" + ExampleConstant.UserId + "]") @RequestParam("uid") String[] uid);


    @ApiOperation(value = "分页查询角色组中的用户", notes = "分页查询角色组中的用户", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "listUserFromRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Page<String>> listUserFromRoleGroup(
            @ApiParam(name = "roleGroupId", value = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId,
            @ApiIgnore @PageableDefault Pageable pageable
    );


    @ApiOperation(value = "查询企业里用户所在的角色组", notes = "查询企业里用户所在的角色组", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "listRoleGroupFromEnterpriseUser", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<List<RoleGroupModel>> listRoleGroupFromEnterpriseUser(
            @ApiParam(name = "enterpriseId", value = "企业id", example = ExampleConstant.EnterpriseId) @RequestParam("enterpriseId") String enterpriseId,
            @ApiParam(name = "uid", value = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid
    );


}
