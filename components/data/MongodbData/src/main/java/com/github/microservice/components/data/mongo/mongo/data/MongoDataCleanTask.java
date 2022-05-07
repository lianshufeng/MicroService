package com.github.microservice.components.data.mongo.mongo.data;

import com.github.microservice.components.data.base.data.DataCleanTask;
import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.ParameterizedType;

public abstract class MongoDataCleanTask<T extends SuperEntity> implements DataCleanTask<T> {

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public String taskName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int batchSize() {
        return 20;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {

    }

    /**
     * 获取实体
     *
     * @return
     */
    public Class<? extends SuperEntity> getEntity() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        // 获取第一个类型参数的真实类型
        return (Class<T>) pt.getActualTypeArguments()[0];
    }


}
