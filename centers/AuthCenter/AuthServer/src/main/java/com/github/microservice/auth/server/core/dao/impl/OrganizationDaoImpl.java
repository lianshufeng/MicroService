package com.github.microservice.auth.server.core.dao.impl;

import com.github.microservice.auth.server.core.dao.extend.OrganizationDaoExtend;
import com.github.microservice.auth.server.core.domain.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class OrganizationDaoImpl implements OrganizationDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public boolean disable(String epId, boolean disable) {
        return this.mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(epId)),
                Update.update("disable", disable),
                Organization.class
        ).getModifiedCount() > 0;
    }
}
