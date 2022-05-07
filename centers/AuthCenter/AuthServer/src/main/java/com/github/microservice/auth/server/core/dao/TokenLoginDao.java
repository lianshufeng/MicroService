package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.server.core.dao.extend.TokenLoginDaoExtend;
import com.github.microservice.auth.server.core.domain.TokenLogin;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;

public interface TokenLoginDao extends MongoDao<TokenLogin>, TokenLoginDaoExtend {

    /**
     * 通过令牌查询
     *
     * @param token
     * @return
     */
    TokenLogin findByToken(String token);


    /**
     * 删除令牌
     *
     * @param token
     * @return
     */
    long removeByToken(String token);
}
