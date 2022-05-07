package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.security.type.AuthType;
import com.github.microservice.auth.server.core.dao.extend.AuthResourcesNameDaoExtend;
import com.github.microservice.auth.server.core.domain.AuthResourcesName;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthResourcesNameDao extends MongoDao<AuthResourcesName>, AuthResourcesNameDaoExtend {


    /**
     * 分页查询权限资源
     *
     * @param authType
     * @param pageable
     * @return
     */
    Page<AuthResourcesName> findByAuthType(AuthType authType, Pageable pageable);

}
