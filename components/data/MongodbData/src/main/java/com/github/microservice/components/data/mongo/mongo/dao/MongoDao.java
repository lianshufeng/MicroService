package com.github.microservice.components.data.mongo.mongo.dao;

import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Mongodb的一些通用方法
 *
 * @param <T>
 * @作者 练书锋
 * @时间 2016年5月13日
 */
public interface MongoDao<T extends SuperEntity> extends MongoRepository<T, String> {




}
