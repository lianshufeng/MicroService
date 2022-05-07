package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.server.core.dao.extend.ApplicationClientDaoExtend;
import com.github.microservice.auth.server.core.domain.ApplicationClient;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;

public interface ApplicationClientDao extends MongoDao<ApplicationClient>, ApplicationClientDaoExtend {

    /**
     * 通过客户端id查询
     *
     * @param clientId
     * @return
     */
    ApplicationClient findByClientId(String clientId);

    /**
     * 客户端ID是否存在
     *
     * @param clientId
     * @return
     */
    boolean existsByClientId(String clientId);

}
