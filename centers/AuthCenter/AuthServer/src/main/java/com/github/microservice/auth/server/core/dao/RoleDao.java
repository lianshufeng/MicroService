package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.server.core.dao.extend.RoleDaoExtend;
import com.github.microservice.auth.server.core.domain.Enterprise;
import com.github.microservice.auth.server.core.domain.Role;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;

import java.util.Set;

public interface RoleDao extends MongoDao<Role>, RoleDaoExtend {


    /**
     * 批量查询
     *
     * @param ids
     * @return
     */
    Set<Role> findByIdIn(Set<String> ids);

    /**
     * 角色组总是否存在
     *
     * @param name
     * @return
     */
    boolean existsByEnterpriseAndName(Enterprise enterprise, String name);


    /**
     * 根据角色名查询角色
     *
     * @param id
     * @return
     */
    Role findTop1ById(String id);


    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    long removeById(String id);


}
