package com.github.microservice.auth.server.core.dao.impl;

import com.github.microservice.auth.client.model.UserLoginModel;
import com.github.microservice.auth.server.core.dao.extend.UserDaoExtend;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class UserDaoImpl implements UserDaoExtend {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;


    @Override
    public boolean updateLoginName(UserLoginModel userLogin) {
        if (this.mongoTemplate.exists(Query.query(Criteria.where(userLogin.getLoginType().name()).is(userLogin.getLoginValue())), User.class)) {
            return false;
        }
        return this.mongoTemplate.updateFirst(
                Query.query(Criteria.where("id").is(userLogin.getId())),
                Update.update(userLogin.getLoginType().name(), userLogin.getLoginValue()),
                User.class
        ).getModifiedCount() > 0;
    }

    @Override
    public boolean disable(String uid, boolean disable) {
        return this.mongoTemplate.updateFirst(
                Query.query(Criteria.where("id").is(uid)),
                Update.update("disable", disable),
                User.class
        ).getModifiedCount() > 0;
    }

    @Override
    public boolean updatePassword(String uid, String passWord) {
        return this.mongoTemplate.updateFirst(
                Query.query(Criteria.where("id").is(uid)),
                Update.update("passWord", passWord),
                User.class
        ).getModifiedCount() > 0;
    }

    @Override
    public boolean unRegister(String uid) {
        Update update = new Update();
        update.set("userName", null);
        update.set("phone", null);
        update.set("email", null);
        update.set("idcard", null);
        update.set("disable", true);
        this.dbHelper.updateTime(update);
        return this.mongoTemplate.updateFirst(
                Query.query(Criteria.where("id").is(uid)),
                update,
                User.class
        ).getModifiedCount() > 0;
    }
}
