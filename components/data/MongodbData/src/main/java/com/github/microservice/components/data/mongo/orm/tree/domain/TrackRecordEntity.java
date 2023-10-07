package com.github.microservice.components.data.mongo.orm.tree.domain;


import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * 记录追踪实体(流水)
 */
@Data
public abstract class TrackRecordEntity<T extends TreeEntity> extends SuperEntity {

    //目标实体对象
    @Indexed
    @DBRef(lazy = true)
    private T entity;




}
