package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.server.core.dao.extend.RoleGroupDaoExtend;
import com.github.microservice.auth.server.core.domain.Organization;
import com.github.microservice.auth.server.core.domain.Role;
import com.github.microservice.auth.server.core.domain.RoleGroup;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;

import java.util.List;
import java.util.Set;

public interface RoleGroupDao extends MongoDao<RoleGroup>, RoleGroupDaoExtend {

    /**
     * 查询该角色所在多少角色组里
     *
     * @param roles
     * @return
     */
    List<RoleGroup> findByRolesIn(Set<Role> roles);


    /**
     * 查询指定角色在角色组的角色组
     */
    List<RoleGroup> findByIdIsAndRolesIn(String id, Role[] roles);


    /**
     * 批量查询角色组
     *
     * @param ids
     * @return
     */
    List<RoleGroup> findByIdIn(Set<String> ids);


    /**
     * 查询企业里的所有角色组
     *
     * @param organization
     * @return
     */
    List<RoleGroup> findByOrganization(Organization organization);


    /**
     * 通过身份查询角色组
     *
     * @param identity
     * @return
     */
    List<RoleGroup> findByOrganizationAndIdentityIn(Organization Organization, Set<String> identity);


    /**
     * 企业和权限组名是否存在
     *
     * @param Organization
     * @param name
     * @return
     */
    boolean existsByOrganizationAndName(Organization Organization, String name);


    RoleGroup findTop1ByOrganizationAndName(Organization Organization, String name);

    /**
     * 通过id查询角色组
     *
     * @param id
     * @return
     */
    RoleGroup findTop1ById(String id);


    /**
     * 通过id删除角色组
     *
     * @param id
     * @return
     */
    Long removeById(String id);


}
