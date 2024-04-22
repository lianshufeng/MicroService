package com.github.microservice.auth.server.core.service.user.mode;

import com.github.microservice.auth.client.example.ExampleConstant;
import com.github.microservice.auth.client.model.UserAuthModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalUserTokenLoginModel extends UserAuthModel {

    //用户id
    @NotNull
    @Schema(name = "maxCheckCount", required = true, example = ExampleConstant.UserId)
    private String uid;


    //最大校验次数
    @Min(value = 0, message = "校验次数的最小值不能低于0次")
    @Schema(name = "maxCheckCount", required = false, example = "3")
    private int maxCheckCount = 3;

    //过期时间, 单位秒
    @Min(value = 0, message = "过期时间最小值不能低于0秒")
    @Schema(name = "timeOut", required = false, example = "300")
    private long timeOut = 5 * 60;
}
