package com.github.microservice.auth.client.model;

import com.github.microservice.auth.security.type.AuthType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "enterpriseModel", description = "企业模型")
public class EnterpriseModel {

    //企业id
    @ApiModelProperty(name = "id", required = false, hidden = true)
    private String id;

    //名称
    @ApiModelProperty(name = "name", required = true, example = "test")
    private String name;

    //备注
    @ApiModelProperty(name = "remark", required = true, example = "测试的企业")
    private String remark;

    //权限类型
    @ApiModelProperty(name = "authType", required = true, example = "Enterprise")
    private AuthType authType ;

}
