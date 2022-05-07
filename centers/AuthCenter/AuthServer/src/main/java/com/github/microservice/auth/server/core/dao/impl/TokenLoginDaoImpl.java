package com.github.microservice.auth.server.core.dao.impl;

import com.github.microservice.auth.server.core.dao.extend.TokenLoginDaoExtend;
import com.github.microservice.auth.server.core.domain.TokenLogin;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


public class TokenLoginDaoImpl implements TokenLoginDaoExtend {

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public boolean incCheckCount(String token) {
        Query query = Query.query(Criteria.where("token").is(token));
        Update update = new Update();
        update.inc("currentCheckCount", 1);
        this.dbHelper.updateTime(update);
        return this.mongoTemplate.updateFirst(query, update, TokenLogin.class).getModifiedCount() > 0;
    }
}
