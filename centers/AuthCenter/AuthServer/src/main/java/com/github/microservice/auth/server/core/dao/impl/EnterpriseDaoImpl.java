package com.github.microservice.auth.server.core.dao.impl;

import com.github.microservice.auth.server.core.dao.EnterpriseDao;
import com.github.microservice.auth.server.core.dao.extend.EnterpriseDaoExtend;
import com.github.microservice.auth.server.core.domain.Enterprise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class EnterpriseDaoImpl implements EnterpriseDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public boolean disable(String epId, boolean disable) {
        return this.mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(epId)),
                Update.update("disable", disable),
                Enterprise.class
        ).getModifiedCount() > 0;
    }
}
