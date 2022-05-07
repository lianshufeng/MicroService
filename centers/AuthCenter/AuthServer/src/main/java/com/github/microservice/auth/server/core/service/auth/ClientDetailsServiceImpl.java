package com.github.microservice.auth.server.core.service.auth;

import com.github.microservice.auth.server.core.auth.endpoint.AuthHelper;
import com.github.microservice.auth.server.core.dao.ApplicationClientDao;
import com.github.microservice.auth.server.core.domain.ApplicationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@Primary
public class ClientDetailsServiceImpl implements ClientDetailsService, Serializable {

    @Autowired
    private ApplicationClientDao applicationClientDao;


    @Autowired
    private AuthHelper authHelper;

    public final static Set<String> Scope = Set.of("all");

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        final ApplicationClient applicationClient = this.applicationClientDao.findByClientId(clientId);
        Assert.isTrue(applicationClient != null, "应用客户端错误");

        return new ClientDetails() {
            @Override
            public String getClientId() {
                return clientId;
            }

            @Override
            public Set<String> getResourceIds() {
                return Set.of("oauth2-resource");
            }

            @Override
            public boolean isSecretRequired() {
                return true;
            }

            @Override
            public String getClientSecret() {
                return applicationClient.getSecret();
            }

            @Override
            public boolean isScoped() {
                return true;
            }

            @Override
            public Set<String> getScope() {
                return Scope;
            }

            @Override
            public Set<String> getAuthorizedGrantTypes() {
                return applicationClient.getAuthorizedGrantTypes();
            }

            @Override
            public Set<String> getRegisteredRedirectUri() {
                return null;
            }

            @Override
            public Collection<GrantedAuthority> getAuthorities() {
                return List.of();
            }

            @Override
            public Integer getAccessTokenValiditySeconds() {
                return authHelper.getAuthProcess().getAccessTokenTimeOut();
            }

            @Override
            public Integer getRefreshTokenValiditySeconds() {
                return authHelper.getAuthProcess().getRefreshTokenTimeOut();
            }

            @Override
            public boolean isAutoApprove(String scope) {
                return false;
            }

            @Override
            public Map<String, Object> getAdditionalInformation() {
                return null;
            }
        };
    }
}
