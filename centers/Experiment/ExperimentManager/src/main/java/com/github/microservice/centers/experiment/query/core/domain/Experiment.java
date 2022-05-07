package com.github.microservice.centers.experiment.query.core.domain;

import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Experiment extends SuperEntity {

    //用户组名
    @Indexed(unique = true)
    private String name;

    //映射地址
    @Indexed
    private Map<String,String> mapping;

    //序号
    @Indexed
    private Integer order;
}
