package com.github.microservice.auth.security.resources.impl;

import com.github.microservice.auth.client.model.AuthResourcesNameModel;
import com.github.microservice.auth.client.type.ResourceType;
import com.github.microservice.auth.security.helper.ResourcesAuthHelper;
import com.github.microservice.auth.security.resources.ResourceRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class AuthNameResourceRegister implements ResourceRegister {

    @Autowired
    private ResourcesAuthHelper resourcesAuthHelper;

    @Override
    public Collection<AuthResourcesNameModel> authResources() {
        return resourcesAuthHelper.getResourceInfos().stream()
                .filter((it) -> {
                    return it.getName() != null && it.getRemark() != null;
                })
                .map((it) -> {
                    AuthResourcesNameModel authResourcesNameModel = new AuthResourcesNameModel();
                    authResourcesNameModel.setName(it.getName());
                    authResourcesNameModel.setRemark(it.getRemark());
                    authResourcesNameModel.setResourceType(ResourceType.AuthName);
                    authResourcesNameModel.setAuthType(it.getAuthType());
                    return authResourcesNameModel;
                }).collect(Collectors.toList());
    }
}
