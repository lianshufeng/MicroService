package com.github.microservice.components.data.mongo.timer.event;

import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;

@FunctionalInterface
public interface SimpleTaskTimerEvent<T extends SuperEntity> {

    /**
     * 执行
     *
     * @param entity
     */
    void execute(T entity);
}
