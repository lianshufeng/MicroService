package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.security.type.AuthType;
import com.github.microservice.auth.server.core.dao.extend.EnterpriseDaoExtend;
import com.github.microservice.auth.server.core.domain.Enterprise;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnterpriseDao extends MongoDao<Enterprise>, EnterpriseDaoExtend {

    /**
     * 企业名是否存在
     *
     * @param name
     * @return
     */
    boolean existsByName(String name);


    /**
     * 根据企业id查询企业
     *
     * @param id
     * @return
     */
    Enterprise findTop1ById(String id);

    /**
     * 通过权限类型进行分页查询
     *
     * @param authType
     * @param pageable
     * @return
     */
    Page<Enterprise> findByAuthType(AuthType authType, Pageable pageable);
}
