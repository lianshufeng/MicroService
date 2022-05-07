package com.github.microservice.auth.server.core.dao.extend;

import com.github.microservice.auth.server.core.domain.Role;

import java.util.List;

public interface RoleDaoExtend {


    /**
     * 通过身份明查询角色
     *
     * @param enterpriseId
     * @param identity
     * @return
     */
    List<Role> findByIdentity(String enterpriseId, String... identity);
}
