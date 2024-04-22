package com.github.microservice.auth.client.service;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.example.ExampleConstant;
import com.github.microservice.auth.client.model.RoleGroupModel;
import com.github.microservice.auth.client.model.RoleModel;
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

import java.util.List;
import java.util.Set;

@FeignClient(name = "authserver/manager/role")
public interface RoleService {


    //------------ 角色 ---------------------
    @Operation(summary = "更新角色", description = "更新角色，有id则更新,无id则新增,注:仅更新非null对象")
    @RequestMapping(value = "updateRole", method = RequestMethod.POST)
    ResultContent<String> updateRole(@RequestBody RoleModel roleModel);

    @Operation(summary = "删除一个角色", description = "删除一个角色")
    @RequestMapping(value = "removeRole", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Void> removeRole(
            @Parameter(name = "roleId", description = "角色id", example = ExampleConstant.RoleId) @RequestParam("roleId") String roleId
    );

    @Operation(summary = "查询一个角色", description = "查询角色")
    @RequestMapping(value = "getRole", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<RoleModel> getRole(
            @Parameter(name = "roleId", description = "角色id", example = ExampleConstant.RoleId) @RequestParam("roleId") String roleId
    );

    @Operation(summary = "通过名称查询一个角色", description = "查询角色")
    @RequestMapping(value = "getRoleByName", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<RoleModel> getRoleByName(
            @Parameter(name = "roleName", description = "角色名称", example = ExampleConstant.RoleId) @RequestParam("roleName") String roleName,
            @Parameter(name = "organizationId", description = "机构id", example = ExampleConstant.OrganizationId) @RequestParam("organizationId") String organizationId
    );

    //--------------- 角色组 -------------------
    @Operation(summary = "更新角色组", description = "更新角色组")
    @RequestMapping(value = "updateRoleGroup", method = RequestMethod.POST)
    ResultContent<String> updateRoleGroup(@RequestBody RoleGroupModel roleGroupModel);

    @Operation(summary = "删除一个角色组", description = "删除一个角色组")
    @RequestMapping(value = "removeRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Void> removeRoleGroup(
            @Parameter(name = "roleGroupId", description = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId
    );

    @Operation(summary = "查询一个角色组", description = "查询角色")
    @RequestMapping(value = "getRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<RoleGroupModel> getRoleGroup(
            @Parameter(name = "roleGroupId", description = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId
    );

    @Operation(summary = "通过名称查询一个角色组", description = "查询角色")
    @RequestMapping(value = "getRoleGroupByName", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<RoleGroupModel> getRoleGroupByName(
            @Parameter(name = "roleGroupName", description = "角色组名称", example = ExampleConstant.RoleId) @RequestParam("roleGroupName") String roleGroupName,
            @Parameter(name = "organizationId", description = "机构id", example = ExampleConstant.OrganizationId) @RequestParam("organizationId") String organizationId
    );


    // --------------角色组机构的关系

    @Operation(summary = "查询机构中的角色组", description = "查询机构中的角色组")
    @RequestMapping(value = "listRoleGroupFromOrganization", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<List<RoleGroupModel>> listRoleGroupFromOrganization(
            @Parameter(name = "organizationId", description = "机构id", example = ExampleConstant.OrganizationId) @RequestParam("organizationId") String organizationId
    );


    @Operation(summary = "通过身份查询角色列表", description = "通过身份查询角色列表")
    @RequestMapping(value = "findByIdentity", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<List<RoleGroupModel>> findByIdentity(
            @Parameter(name = "organizationId", description = "机构id", example = ExampleConstant.OrganizationId) @RequestParam("organizationId") String organizationId,
            @Parameter(name = "identity", description = "身份名", example = "[\"" + ExampleConstant.Identity + "\"]") @RequestParam("identity") String[] identity);


    //------------------ 角色组关系 --------------------------------
    @Operation(summary = "在角色组中添加角色", description = "在角色组中添加角色")
    @RequestMapping(value = "addRoleToRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Void> addRoleToRoleGroup(
            @Parameter(name = "roleGroupId", description = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId,
            @Parameter(name = "roleId", description = "角色id", example = ExampleConstant.RoleId) @RequestParam("roleId") String[] roleId);


    @Operation(summary = "删除角色组中的角色", description = "删除角色组中的角色")
    @RequestMapping(value = "removeRoleFromRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Void> removeRoleFromRoleGroup(
            @Parameter(name = "roleGroupId", description = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId,
            @Parameter(name = "roleId", description = "角色id", example = ExampleConstant.RoleId) @RequestParam("roleId") String[] roleId);


    @Operation(summary = "查询角色所在的角色组", description = "查询角色所在的角色组")
    @RequestMapping(value = "listRoleGroupFromRole", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<List<RoleGroupModel>> listRoleGroupFromRole(
            @Parameter(name = "roleId", description = "角色id", example = ExampleConstant.RoleId) @RequestParam("roleId") String[] roleId
    );


    //------------------------ 角色组里添加用户 -------------------------------------------------

    @Operation(summary = "在角色组中添加用户", description = "在角色组中添加用户")
    @RequestMapping(value = "addUserToRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Set<String>> addUserToRoleGroup(
            @Parameter(name = "roleGroupId", description = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId,
            @Parameter(name = "uid", description = "用户id", example = "[\"" + ExampleConstant.UserId + "\"]") @RequestParam("uid") String[] uid);


    @Operation(summary = "用户是否在角色组里", description = "用户是否在角色组里")
    @RequestMapping(value = "hasUserInRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Set<String>> hasUserInRoleGroup(
            @Parameter(name = "roleGroupId", description = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId,
            @Parameter(name = "uid", description = "用户id", example = "[\"" + ExampleConstant.UserId + "\"]") @RequestParam("uid") String[] uid);


    @Operation(summary = "为用户添加多个角色组", description = "在角色组中添加用户")
    @RequestMapping(value = "addUsersToRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Set<String>> addUsersToRoleGroup(
            @Parameter(name = "uid", description = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid,
            @Parameter(name = "roleGroupIds", description = "角色组id", example = "[\"" + ExampleConstant.RoleGroupId + "\"]") @RequestParam("roleGroupIds") String[] roleGroupIds
    );


    @Operation(summary = "在角色组中删除用户", description = "在角色组中删除用户")
    @RequestMapping(value = "removeUserFromRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Set<String>> removeUserFromRoleGroup(
            @Parameter(name = "roleGroupId", description = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId,
            @Parameter(name = "uid", description = "用户id", example = "[\"" + ExampleConstant.UserId + "\"]") @RequestParam("uid") String[] uid);


    @Operation(summary = "分页查询角色组中的用户", description = "分页查询角色组中的用户")
    @RequestMapping(value = "listUserFromRoleGroup", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<Page<String>> listUserFromRoleGroup(
            @Parameter(name = "roleGroupId", description = "角色组id", example = ExampleConstant.RoleGroupId) @RequestParam("roleGroupId") String roleGroupId,
            @Parameter(hidden = true) @PageableDefault Pageable pageable
    );


    @Operation(summary = "查询机构里用户所在的角色组", description = "查询机构里用户所在的角色组")
    @RequestMapping(value = "listRoleGroupFromOrganizationUser", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    ResultContent<List<RoleGroupModel>> listRoleGroupFromOrganizationUser(
            @Parameter(name = "organizationId", description = "机构id", example = ExampleConstant.OrganizationId) @RequestParam("organizationId") String organizationId,
            @Parameter(name = "uid", description = "用户id", example = ExampleConstant.UserId) @RequestParam("uid") String uid
    );


}
