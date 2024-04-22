package com.github.microservice.auth.client.model;

import com.github.microservice.auth.client.type.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "userLoginModel", description = "用户登陆模型")
public class UserLoginModel {

    //用户id
    @Schema(name = "id", required = true, example = "60e7c50b56780e05bf5b977f")
    private String id;

    //登陆类型
    @Schema(name = "loginType", required = true, example = "Phone")
    private LoginType loginType;

    //登陆名
    @Schema(name = "loginValue", required = true, example = "15123241363")
    private String loginValue;
}
