package com.example.mongo.core.dao;

import com.example.mongo.core.dao.extend.UserDaoExtend;
import com.example.mongo.core.domain.User;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;

public interface UserDao extends MongoDao<User>, UserDaoExtend {

    User findByName(String name);
}
