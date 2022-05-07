package com.github.microservice.centers.experiment.query.core.model;

import com.github.microservice.centers.experiment.query.core.constant.ExampleConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupModel {

    //用户组名
    @ApiModelProperty(name = "name", required = true, example = ExampleConstant.UserGroupName)
    private String name;

    //用户组标签
    @ApiModelProperty(name = "uid", required = true, example = "['u001','u002','u003']")
    private String[] uid;

    //关联到的实验环境
    @ApiModelProperty(name = "experiment", required = true, example = "{'name':'"+ExampleConstant.ExperimentName+"'}")
    private ExperimentModel experiment;

}
