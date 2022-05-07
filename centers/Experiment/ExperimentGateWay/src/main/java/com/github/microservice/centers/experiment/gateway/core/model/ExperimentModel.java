package com.github.microservice.centers.experiment.gateway.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperimentModel {

    //用户组名
    private String name;

    //映射地址
    private Map<String, String> mapping;

    //序号
    private Integer order;
}
