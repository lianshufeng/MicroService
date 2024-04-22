package com.github.microservice.auth.client.model;

import com.github.microservice.auth.client.example.ExampleConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Schema(name = "roleModel", description = "角色模型")
public class RoleModel {

    //角色id
    @Schema(name = "id", required = false, example = ExampleConstant.RoleId)
    private String id;

    //角色名
    @Schema(name = "name", required = false, example = ExampleConstant.RoleName)
    private String name;

    //备注
    @Schema(name = "remark", required = false)
    private String remark;

    //权限列表
    @Schema(name = "auth", required = false, example = "[\"listUser\",\"callPhone\"]")
    private Set<String> auth;

    //机构id
    @Schema(name = "organizationId", required = true, example = ExampleConstant.OrganizationId)
    private String organizationId;


}
