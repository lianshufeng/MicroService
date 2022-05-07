package com.github.microservice.centers.experiment.query.core.domain;

import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class UserGroup extends SuperEntity {

    //用户组名
    @Indexed(unique = true)
    private String name;

    //用户组标签
    @Indexed
    private String[] uid;

    //关联到的实验环境
    @Indexed
    @DBRef(lazy = true)
    private Experiment experiment;

}
