package com.github.microservice.auth.server.core.dao.impl;

import com.github.microservice.auth.server.core.dao.RoleDao;
import com.github.microservice.auth.server.core.dao.extend.RoleDaoExtend;
import com.github.microservice.auth.server.core.domain.Organization;
import com.github.microservice.auth.server.core.domain.Role;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Set;

public class RoleDaoImpl implements RoleDaoExtend {

    @Autowired
    private RoleDao roleDao;


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;


    @Override
    public List<Role> findByIdentity(String organizationId, String... identity) {
        Organization organization = Organization.build(organizationId);

        Query query = new Query();
        query.addCriteria(Criteria.where("organization").is(organization).and("identity").in(Set.of(identity)));

        return this.mongoTemplate.find(query, Role.class);
    }
}
