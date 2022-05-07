package com.github.microservice.auth.server.core.dao.extend;

import com.github.microservice.auth.server.core.domain.EnterpriseUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EnterpriseUserDaoExtend {

    /**
     * 查询
     *
     * @param mql
     * @param fields
     * @param pageable
     * @return
     */
    Page<EnterpriseUser> query(String mql, String[] fields, Pageable pageable);


    /**
     * 更新数据
     *
     * @param enterpriseUsers
     * @return
     */
    long update(List<EnterpriseUser> enterpriseUsers);


    /**
     * 查询用户的附属企业
     *
     * @param uid
     * @param pageable
     * @return
     */
    Page<EnterpriseUser> affiliatesEnterprise(String uid, Pageable pageable);


}
