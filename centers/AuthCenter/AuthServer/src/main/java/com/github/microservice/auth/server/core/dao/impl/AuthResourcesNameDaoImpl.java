package com.github.microservice.auth.server.core.dao.impl;

import com.github.microservice.auth.server.core.dao.extend.AuthResourcesNameDaoExtend;
import com.github.microservice.auth.server.core.domain.AuthResourcesName;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.mongodb.bulk.BulkWriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.*;

public class AuthResourcesNameDaoImpl implements AuthResourcesNameDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;


    //存活时间
    private final static long LiveTime = 1000 * 60 * 60 * 24 * 7;


    @Override
    public int update(AuthResourcesName[] authResourcesName) {
        if (authResourcesName == null || authResourcesName.length == 0) {
            return 0;
        }
        List<String> ret = new ArrayList<>();
        final BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, AuthResourcesName.class);
        Arrays.stream(authResourcesName).forEach((resourcesName) -> {
            Query query = Query.query(
                    Criteria
                            .where("resourceType").is(resourcesName.getResourceType())
                            .and("authType").is(resourcesName.getAuthType())
                            .and("name").is(resourcesName.getName())
            );

            Update update = new Update();
            update.setOnInsert("authType", resourcesName.getAuthType());
            update.setOnInsert("name", resourcesName.getName());
            update.setOnInsert("resourceType", resourcesName.getResourceType());
            //更新备注
            Optional.ofNullable(resourcesName.getRemark()).ifPresent((remark) -> {
                update.set("remark", remark);
            });
            update.set("TTL", new Date(this.dbHelper.getTime() + LiveTime));
            this.dbHelper.saveTime(update);

            bulkOperations.upsert(query, update);
        });
        BulkWriteResult bulkWriteResult = bulkOperations.execute();
        return bulkWriteResult.getInsertedCount() + bulkWriteResult.getModifiedCount();
    }
}
