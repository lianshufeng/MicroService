package com.github.microservice.centers.experiment.query.core.model;

import com.github.microservice.centers.experiment.query.core.constant.ExampleConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperimentModel {

    //用户组名
    @ApiModelProperty(name = "name", required = true, example = ExampleConstant.ExperimentName)
    private String name;

    //映射地址
    @ApiModelProperty(name = "name", required = true, example = "{'ExperimentService1':'ExperimentService2'}")
    private Map<String, String> mapping;

    //序号
    @ApiModelProperty(name = "order", required = true, example = "10")
    private Integer order;
}
