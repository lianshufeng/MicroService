package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.server.core.domain.UserLog;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;

public interface UserLogDao extends MongoDao<UserLog> {
}
