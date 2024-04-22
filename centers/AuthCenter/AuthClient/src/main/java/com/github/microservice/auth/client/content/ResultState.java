package com.github.microservice.auth.client.content;

import lombok.Getter;

/**
 * 结果状态模板
 */
public enum ResultState {

    Success("成功"),
    Fail("失败"),
    Error("错误"),
    Exception("异常"),
    Robot("机器验证"),

    UserNotNull("用户不能为空"),
    UserExists("用户存在"),
    UserNotExists("用户不存在"),
    UserPasswordError("用户密码不正确"),
    UserDisable("用户被禁用"),

    ClientExists("应用客户端存在"),

    AccessTokenError("访问令牌错误"),
    RefreshTokenError("刷新令牌错误"),


    RoleExists("角色存在"),
    RoleNotExists("角色不存在"),
    RoleNameNotNull("角色名不能为空"),
    RoleOrganizationDoNotUpdate("不允许修改角色的机构"),


    RoleGroupExists("角色组存在"),
    RoleGroupNotExists("角色组不存在"),
    RoleGroupNameNotNull("角色组名不能为空"),
    RoleGroupOrganizationDoNotUpdate("不允许修改角色的机构"),

    OrganizationNotExist("机构不存在"),
    OrganizationNameExist("机构名已存在"),
    OrganizationAuthTypeDoNotUpdate("不能创建/更新该机构类型"),
    OrganizationNotNull("机构不能为空"),

    AuthTypeExist("权限类型存在"),


    TokenLoginParmError("令牌登陆的参数类型错误 "),

    ;

    @Getter
    private String remark;

    ResultState(String remark) {
        this.remark = remark;
    }
}
