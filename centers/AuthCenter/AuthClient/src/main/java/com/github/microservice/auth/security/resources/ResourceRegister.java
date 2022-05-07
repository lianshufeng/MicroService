package com.github.microservice.auth.security.resources;

import com.github.microservice.auth.client.model.AuthResourcesNameModel;

import java.util.Collection;

public interface ResourceRegister {

    /**
     * 权限资源
     *
     * @return
     */
    Collection<AuthResourcesNameModel> authResources();

}
