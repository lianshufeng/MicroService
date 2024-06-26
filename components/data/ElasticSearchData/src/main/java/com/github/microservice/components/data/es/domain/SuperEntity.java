package com.github.microservice.components.data.es.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

/**
 * 所有对象的父类
 *
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年1月5日
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Document(indexName = "superentity")
public abstract class SuperEntity  implements Serializable {


    @Id
    private String id;


    /**
     * 创建时间
     */

    @CreatedDate
    @Field(type = FieldType.Long, index = true)
    private Long createTime;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Field(type = FieldType.Long, index = true)
    private Long updateTime;


}
