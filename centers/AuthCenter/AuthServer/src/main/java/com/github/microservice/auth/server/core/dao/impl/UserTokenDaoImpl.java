package com.github.microservice.auth.server.core.dao.impl;

import com.github.microservice.auth.client.type.DeviceType;
import com.github.microservice.auth.server.core.dao.extend.UserTokenDaoExtend;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.auth.server.core.domain.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.List;

public class UserTokenDaoImpl implements UserTokenDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<UserToken> findUserToken(DeviceType deviceType, String uid) {
        Criteria criteria = new Criteria();
        if (deviceType != null) {
            criteria = criteria.and("deviceType").is(deviceType);
        }
        criteria = criteria.and("user").is(User.build(uid));
        return this.mongoTemplate.find(new Query(criteria), UserToken.class);
    }

    @Override
    public boolean setTTL(String refreshToken, long time) {
        return this.mongoTemplate.updateFirst(
                Query.query(Criteria.where("refreshToken").is(refreshToken)),
                Update.update("TTL", new Date(time)),
                UserToken.class
        ).getModifiedCount() > 0;
    }
}
