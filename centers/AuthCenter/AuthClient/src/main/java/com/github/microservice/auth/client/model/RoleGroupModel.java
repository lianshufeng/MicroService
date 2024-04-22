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
@Schema(name = "roleGroupModel", description = "角色模型")
public class RoleGroupModel {
    //企业id
    @Schema(name = "organizationId", required = true, example = ExampleConstant.OrganizationId)
    private String organizationId;

    //角色组id
    @Schema(name = "id", required = false, example = ExampleConstant.RoleGroupId)
    private String id;

    //角色组名称
    @Schema(name = "name", required = false, example = ExampleConstant.RoleGroupName)
    private String name;

    //备注
    @Schema(name = "remark", required = false)
    private String remark;

    //身份
    @Schema(name = "identity", required = false, example = "[\""+ ExampleConstant.Identity +"\"]")
    private Set<String> identity;

    //角色id
    @Schema(name = "roleId", required = false, example = "[\"" + ExampleConstant.RoleId + "\"]")
    private Set<String> roleId;

}
