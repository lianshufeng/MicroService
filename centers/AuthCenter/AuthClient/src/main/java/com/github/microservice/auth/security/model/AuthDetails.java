package com.github.microservice.auth.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthDetails extends UserTokenCacheItem {


    //企业信息
    @Delegate
    private EnterpriseUserCacheModel enterpriseUserModel = new EnterpriseUserCacheModel();


}
