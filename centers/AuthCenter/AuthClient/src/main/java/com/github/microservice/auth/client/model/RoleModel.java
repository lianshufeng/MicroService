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
@ApiModel(value = "roleModel", description = "角色模型")
public class RoleModel {

    //角色id
    @ApiModelProperty(name = "id", required = false, example = ExampleConstant.RoleId)
    private String id;

    //角色名
    @ApiModelProperty(name = "name", required = false, example = ExampleConstant.RoleName)
    private String name;

    //备注
    @ApiModelProperty(name = "remark", required = false)
    private String remark;

    //权限列表
    @ApiModelProperty(name = "auth", required = false, example = "[" + ExampleConstant.AuthName + "]")
    private Set<String> auth;

    //企业id
    @ApiModelProperty(name = "enterpriseId", required = true, example = ExampleConstant.EnterpriseId)
    private String enterpriseId;


}
