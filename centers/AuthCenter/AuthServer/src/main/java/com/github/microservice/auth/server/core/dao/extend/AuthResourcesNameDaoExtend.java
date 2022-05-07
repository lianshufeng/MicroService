package com.github.microservice.auth.server.core.dao.extend;

import com.github.microservice.auth.server.core.domain.AuthResourcesName;

public interface AuthResourcesNameDaoExtend {


    /**
     * 更新资源
     */
    int update(AuthResourcesName[] authResourcesName);
}
