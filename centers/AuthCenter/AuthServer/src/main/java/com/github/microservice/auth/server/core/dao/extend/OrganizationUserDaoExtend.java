package com.github.microservice.auth.server.core.dao.extend;

import com.github.microservice.auth.server.core.domain.OrganizationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrganizationUserDaoExtend {

    /**
     * 查询
     *
     * @param mql
     * @param fields
     * @param pageable
     * @return
     */
    Page<OrganizationUser> query(String mql, String[] fields, Pageable pageable);


    /**
     * 更新数据
     *
     * @param organizationUsers
     * @return
     */
    long update(List<OrganizationUser> organizationUsers);


    /**
     * 查询用户的附属企业
     *
     * @param uid
     * @param pageable
     * @return
     */
    Page<OrganizationUser> affiliatesOrganization(String uid, Pageable pageable);


}
