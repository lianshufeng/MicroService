package com.github.microservice.components.data.mongo.mongo.data.dao;

import com.github.microservice.components.data.mongo.mongo.data.MongoDataCleanTask;
import com.github.microservice.components.data.mongo.mongo.data.domain.DataCleanTask;

import java.math.BigDecimal;

public interface DataCleanTaskDao {

    /**
     * 执行任务
     *
     * @param mongoDataCleanTask
     * @return
     */
    DataCleanTask executeTask(MongoDataCleanTask mongoDataCleanTask);


    /**
     * 激活任务
     *
     * @param taskName
     * @return
     */
    boolean activeTask(String taskName);


    /**
     * 设置任务进度
     *
     * @param taskName
     * @param rate
     */
    void setTaskprogress(String taskName, BigDecimal rate);


}
