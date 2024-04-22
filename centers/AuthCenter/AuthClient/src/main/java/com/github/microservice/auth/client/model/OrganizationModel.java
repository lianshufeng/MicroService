package com.github.microservice.auth.client.model;

import com.github.microservice.auth.client.example.ExampleConstant;
import com.github.microservice.auth.security.type.AuthType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "organizationModel", description = "企业模型")
public class OrganizationModel {

    //机构id
    @Schema(name = "id", required = false, example = ExampleConstant.OrganizationId)
    private String id;

    //名称
    @Schema(name = "name", required = true, example = "test")
    private String name;

    //备注
    @Schema(name = "remark", required = true, example = "测试的企业")
    private String remark;

    //权限类型
    @Schema(name = "authType", required = true, example = "Enterprise")
    private AuthType authType;

}
