package com.github.microservice.auth.client.model;

import com.github.microservice.auth.client.example.ExampleConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Set;

@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "roleGroupModel", description = "角色模型")
public class RoleGroupModel {
    //企业id
    @ApiModelProperty(name = "enterpriseId", required = true, example = ExampleConstant.EnterpriseId)
    private String enterpriseId;

    //角色组id
    @ApiModelProperty(name = "id", required = false, example = ExampleConstant.RoleGroupId)
    private String id;

    //角色组名称
    @ApiModelProperty(name = "name", required = false, example = ExampleConstant.RoleGroupName)
    private String name;

    //备注
    @ApiModelProperty(name = "remark", required = false)
    private String remark;

    //身份
    @ApiModelProperty(name = "identity", required = false, example = "[" + ExampleConstant.Identity + "]")
    private Set<String> identity;

    //角色id
    @ApiModelProperty(name = "roleId", required = false, example = "[" + ExampleConstant.RoleId + "]")
    private Set<String> roleId;

}
