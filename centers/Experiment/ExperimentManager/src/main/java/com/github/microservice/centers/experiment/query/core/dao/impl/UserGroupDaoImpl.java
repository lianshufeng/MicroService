package com.github.microservice.centers.experiment.query.core.dao.impl;

import com.github.microservice.centers.experiment.query.core.dao.extend.UserGroupDaoExtend;
import com.github.microservice.centers.experiment.query.core.domain.UserGroup;
import com.github.microservice.components.data.base.util.PageEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

public class UserGroupDaoImpl implements UserGroupDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<UserGroup> list(String uid, Pageable pageable) {
        final Query query = new Query(StringUtils.hasText(uid) ? Criteria.where("uid").is(uid) : new Criteria());
        Long count = this.mongoTemplate.count(query, UserGroup.class);
        query.with(pageable);
        return new PageImpl<UserGroup>(
                this.mongoTemplate.find(query, UserGroup.class),
                pageable,
                count
        );
    }
}
