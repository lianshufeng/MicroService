package com.github.microservice.auth.client.model;

import com.github.microservice.auth.client.example.ExampleConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "tokenLoginModel", description = " 令牌登陆模型")
public class TokenLoginModel {

    //用户id
    @NotNull
    @ApiModelProperty(name = "uid", required = true, example = ExampleConstant.UserId)
    private String uid;

    //临时密钥
    @NotNull
    @ApiModelProperty(name = "vaue", required = true, example = "xiaofeng")
    private String value;

    //最大校验次数
    @Min(value = 0, message = "校验次数的最小值不能低于0次")
    @ApiModelProperty(name = "maxCheckCount", required = false, example = "3")
    private int maxCheckCount = 3;

    //过期时间, 单位秒
    @Min(value = 0, message = "过期时间最小值不能低于0秒")
    @ApiModelProperty(name = "timeOut", required = false, example = "300")
    private long timeOut = 5 * 60;
}
