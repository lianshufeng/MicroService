package com.github.microservice.components.data.mongo.mongo.helper;

import com.mongodb.ReadPreference;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoTemplateNearest {


    @Delegate(types = MongoTemplate.class)
    private MongoTemplate mongoTemplate;


    @Autowired
    private void init(MongoTemplate mongoTemplate) {

        //实例化 mongoTemplate
        mongoTemplate = new MongoTemplate(
                mongoTemplate.getMongoDatabaseFactory(),
                mongoTemplate.getConverter()
        );

        //  最小的延迟，会从复制集中选择最低网络延迟的节点处理读请求，这种方式不能保证数据一致性，也不能保证减少IO以及CPU的负载
        mongoTemplate.setReadPreference(ReadPreference.nearest());
    }

}
