package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.security.type.AuthType;
import com.github.microservice.auth.server.core.dao.extend.OrganizationDaoExtend;
import com.github.microservice.auth.server.core.domain.Organization;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrganizationDao extends MongoDao<Organization>, OrganizationDaoExtend {


    /**
     * 是否该类型的机构存在
     *
     * @param authType
     * @return
     */
    boolean existsByAuthType(AuthType authType);


    /**
     * 机构名是否存在
     *
     * @param name
     * @return
     */
    boolean existsByName(String name);


    /**
     * 根据机构id查询机构
     *
     * @param id
     * @return
     */
    Organization findTop1ById(String id);


    /**
     * 根据机构名称查询
     *
     * @param name
     * @return
     */
    Organization findByName(String name);


    /**
     * 通过权限类型进行分页查询
     *
     * @param authType
     * @param pageable
     * @return
     */
    Page<Organization> findByAuthType(AuthType authType, Pageable pageable);
}
