package com.github.microservice.auth.server.core.dao.extend;

import com.github.microservice.auth.server.core.domain.Role;
import com.github.microservice.auth.server.core.domain.RoleGroup;

import java.util.Collection;
import java.util.Set;

public interface RoleGroupDaoExtend {


    /**
     * 在角色组中删除角色
     *
     * @param roleGroup
     * @return
     */
    boolean removeRole(RoleGroup roleGroup, Collection<Role> roles);


    /**
     * 在角色组中添加角色
     *
     * @param roleGroup
     * @return
     */
    boolean addRole(RoleGroup roleGroup, Collection<Role> roles);

}
