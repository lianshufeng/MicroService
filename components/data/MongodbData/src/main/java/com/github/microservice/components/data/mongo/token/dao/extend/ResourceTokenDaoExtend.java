package com.github.microservice.components.data.mongo.token.dao.extend;

import com.github.microservice.components.data.mongo.token.domain.ResourceToken;

public interface ResourceTokenDaoExtend {


    /**
     * 计数器
     *
     * @param resourceName
     * @return
     */
    long counter(String resourceName);

    /**
     * 更新记录
     *
     * @param resourceName
     * @param counter
     */
    void record(String resourceName, long counter, long ttl);


    /**
     * 更新时间戳
     */
    void updateTTL(long ttl, ResourceToken... resourceTokens);


}
