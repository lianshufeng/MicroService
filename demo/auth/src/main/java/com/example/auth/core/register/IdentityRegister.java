package com.example.auth.core.register;

import com.github.microservice.auth.client.model.AuthResourcesNameModel;
import com.github.microservice.auth.client.type.ResourceType;
import com.github.microservice.auth.security.resources.ResourceRegister;
import com.github.microservice.auth.security.type.AuthType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 注册权限中的角色身份
 */
@Component
public class IdentityRegister implements ResourceRegister {
    @Override
    public Collection<AuthResourcesNameModel> authResources() {
        return List.of(
                AuthResourcesNameModel.builder().authType(AuthType.Platform).name("user").remark("用户").resourceType(ResourceType.IdentityName).build(),
                AuthResourcesNameModel.builder().authType(AuthType.Platform).name("team").remark("团队").resourceType(ResourceType.IdentityName).build()
        );
    }
}
