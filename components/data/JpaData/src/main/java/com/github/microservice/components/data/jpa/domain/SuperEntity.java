package com.github.microservice.components.data.jpa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
public abstract class SuperEntity extends AbstractPersistable<Long> implements Serializable {

    @CreatedDate
    @Column(name = "createTime")
    private Long createTime;

    @LastModifiedDate
    @Column(name = "updateTime")
    private Long updateTime;

}
