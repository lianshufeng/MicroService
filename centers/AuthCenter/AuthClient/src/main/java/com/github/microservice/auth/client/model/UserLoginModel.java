package com.github.microservice.auth.client.model;

import com.github.microservice.auth.client.type.LoginType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "userLoginModel", description = "用户登陆模型")
public class UserLoginModel {

    //用户id
    @ApiModelProperty(name = "id", required = true, example = "60e7c50b56780e05bf5b977f")
    private String id;

    //登陆类型
    @ApiModelProperty(name = "loginType", required = true, example = "Phone")
    private LoginType loginType;

    //登陆名
    @ApiModelProperty(name = "loginValue", required = true, example = "15123241363")
    private String loginValue;
}
