package com.github.microservice.auth.server.core.service.user.mode;

import com.github.microservice.auth.client.example.ExampleConstant;
import com.github.microservice.auth.client.model.UserAuthModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalUserTokenLoginModel extends UserAuthModel {

    //用户id
    @NotNull
    @ApiModelProperty(name = "maxCheckCount", required = true, example = ExampleConstant.UserId)
    private String uid;


    //最大校验次数
    @Min(value = 0, message = "校验次数的最小值不能低于0次")
    @ApiModelProperty(name = "maxCheckCount", required = false, example = "3")
    private int maxCheckCount = 3;

    //过期时间, 单位秒
    @Min(value = 0, message = "过期时间最小值不能低于0秒")
    @ApiModelProperty(name = "timeOut", required = false, example = "300")
    private long timeOut = 5 * 60;
}
