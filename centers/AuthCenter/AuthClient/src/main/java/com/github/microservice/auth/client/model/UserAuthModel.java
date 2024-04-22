package com.github.microservice.auth.client.model;

import com.github.microservice.auth.client.type.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户权限模型
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "userAuthModel", description = "用户授权模型")
public class UserAuthModel {

    //登陆类型
    @Schema(name = "loginType", required = true, example = "Phone")
    private LoginType loginType;

    //登陆名
    @Schema(name = "loginValue", required = true, example = "15123241353")
    private String loginValue;

    //密码
    @Schema(name = "passWord", required = true, example = "xiaofeng")
    private String passWord;
}
