package com.github.microservice.auth.server.core.dao.extend;

public interface OrganizationDaoExtend {

    /**
     * 禁用
     *
     * @param epId
     * @return
     */
    boolean disable(String epId, boolean disable);


}
