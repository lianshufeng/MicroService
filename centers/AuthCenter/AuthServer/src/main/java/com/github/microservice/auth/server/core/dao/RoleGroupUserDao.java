package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.server.core.dao.extend.RoleGroupUserDaoExtend;
import com.github.microservice.auth.server.core.domain.Enterprise;
import com.github.microservice.auth.server.core.domain.RoleGroup;
import com.github.microservice.auth.server.core.domain.RoleGroupUser;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface RoleGroupUserDao extends MongoDao<RoleGroupUser>, RoleGroupUserDaoExtend {


    /**
     * 查询企业中的用户所在的角色组
     *
     * @param enterprise
     * @param user
     * @return
     */
    List<RoleGroupUser> findByEnterpriseAndUserIn(Enterprise enterprise, User... user);


    /**
     * 删除角色组里所在的用户
     *
     * @param roleGroup
     * @param user
     * @return
     */
    long removeByRoleGroupAndUser(RoleGroup roleGroup, User user);


    /**
     * 删除角色组里所有的用户
     *
     * @param roleGroup
     * @return
     */
    long removeByRoleGroup(RoleGroup roleGroup);


    /**
     * 查询角色组中所有的用户关系
     *
     * @param roleGroup
     * @return
     */
    List<RoleGroupUser> findByRoleGroup(RoleGroup roleGroup);


    /**
     * 分页查询角色组中的用户
     *
     * @param roleGroup
     * @param pageable
     * @return
     */
    Page<RoleGroupUser> findByRoleGroup(RoleGroup roleGroup, Pageable pageable);

    /**
     * 批量查询所有的用户关系
     *
     * @param roleGroups
     * @return
     */
    List<RoleGroupUser> findByRoleGroupIn(Collection<RoleGroup> roleGroups);


    /**
     * 角色组中是否有该用户
     *
     * @param roleGroup
     * @param user
     * @return
     */
    boolean existsByRoleGroupAndUser(RoleGroup roleGroup, User user);


}
