package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.server.core.dao.extend.EnterpriseUserDaoExtend;
import com.github.microservice.auth.server.core.domain.Enterprise;
import com.github.microservice.auth.server.core.domain.EnterpriseUser;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnterpriseUserDao extends MongoDao<EnterpriseUser>, EnterpriseUserDaoExtend {


    /**
     * 通过企业和用户查询
     *
     * @param enterprise
     * @param user
     * @return
     */
    EnterpriseUser findByEnterpriseAndUser(Enterprise enterprise, User user);


    /**
     * 查询企业
     *
     * @param user
     * @param pageable
     * @return
     */
    Page<EnterpriseUser> findByUser(User user, Pageable pageable);
}
