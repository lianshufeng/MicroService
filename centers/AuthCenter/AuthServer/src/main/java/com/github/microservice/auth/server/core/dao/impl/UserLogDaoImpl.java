package com.github.microservice.auth.server.core.dao.impl;

import com.github.microservice.auth.server.core.dao.extend.UserLogDaoExtend;
import com.github.microservice.auth.server.core.domain.UserLog;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

public class UserLogDaoImpl implements UserLogDaoExtend {

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<UserLog> list(Pageable pageable) {
        return this.dbHelper.pages(new Query(), pageable, UserLog.class);
    }
}
