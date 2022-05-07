package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.auth.server.core.domain.UserLoginLog;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserLoginLogDao extends MongoDao<UserLoginLog> {

    /**
     * 分页查询用户的登陆日志
     *
     * @param user
     * @param pageable
     * @return
     */
    Page<UserLoginLog> findByUser(User user, Pageable pageable);

}