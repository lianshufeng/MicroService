package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.server.core.dao.extend.OrganizationUserDaoExtend;
import com.github.microservice.auth.server.core.domain.Organization;
import com.github.microservice.auth.server.core.domain.OrganizationUser;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrganizationUserDao extends MongoDao<OrganizationUser>, OrganizationUserDaoExtend {


    /**
     * 通过企业和用户查询
     *
     * @param organization
     * @param user
     * @return
     */
    OrganizationUser findByOrganizationAndUser(Organization organization, User user);


    /**
     * 查询企业
     *
     * @param user
     * @param pageable
     * @return
     */
    Page<OrganizationUser> findByUser(User user, Pageable pageable);
}
