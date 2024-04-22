/*
 * Copyright 2006-2011 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.github.microservice.auth.server.core.oauth2.authentication;

import java.util.Collection;
import java.util.Set;

import com.github.microservice.auth.server.core.oauth2.client.ClientDetails;
import com.github.microservice.auth.server.core.oauth2.details.OAuth2AuthenticationDetails;
import com.github.microservice.auth.server.core.oauth2.exception.ClientRegistrationException;
import com.github.microservice.auth.server.core.oauth2.exception.InvalidTokenException;
import com.github.microservice.auth.server.core.oauth2.exception.OAuth2AccessDeniedException;
import com.github.microservice.auth.server.core.oauth2.service.ClientDetailsService;
import com.github.microservice.auth.server.core.oauth2.service.ResourceServerTokenServices;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

/**
 * An {@link AuthenticationManager} for OAuth2 protected resources.
 *
 * <p>
 * @deprecated See the <a href="https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth 2.0 Migration Guide</a> for Spring Security 5.
 *
 * @author Dave Syer
 * 
 */
@Deprecated
public class OAuth2AuthenticationManager implements AuthenticationManager, InitializingBean {

	private ResourceServerTokenServices tokenServices;

	private ClientDetailsService clientDetailsService;

	private String resourceId;

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public void setClientDetailsService(ClientDetailsService clientDetailsService) {
		this.clientDetailsService = clientDetailsService;
	}

	/**
	 * @param tokenServices the tokenServices to set
	 */
	public void setTokenServices(ResourceServerTokenServices tokenServices) {
		this.tokenServices = tokenServices;
	}

	public void afterPropertiesSet() {
		Assert.state(tokenServices != null, "TokenServices are required");
	}

	/**
	 * Expects the incoming authentication request to have a principal value that is an access token value (e.g. from an
	 * authorization header). Loads an authentication from the {@link ResourceServerTokenServices} and checks that the
	 * resource id is contained in the {@link AuthorizationRequest} (if one is specified). Also copies authentication
	 * details over from the input to the output (e.g. typically so that the access token value and request details can
	 * be reported later).
	 * 
	 * @param authentication an authentication request containing an access token value as the principal
	 * @return an {@link OAuth2Authentication}
	 * 
	 * @see org.springframework.security.authentication.AuthenticationManager#authenticate(org.springframework.security.core.Authentication)
	 */
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		if (authentication == null) {
			throw new InvalidTokenException("Invalid token (token not found)");
		}
		String token = (String) authentication.getPrincipal();
		OAuth2Authentication auth = tokenServices.loadAuthentication(token);
		if (auth == null) {
			throw new InvalidTokenException("Invalid token");
		}

		Collection<String> resourceIds = auth.getOAuth2Request().getResourceIds();
		if (resourceId != null && resourceIds != null && !resourceIds.isEmpty() && !resourceIds.contains(resourceId)) {
			throw new OAuth2AccessDeniedException("Invalid token does not contain resource id (" + resourceId + ")");
		}

		checkClientDetails(auth);

		if (authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
			OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
			// Guard against a cached copy of the same details
			if (!details.equals(auth.getDetails())) {
				// Preserve the authentication details from the one loaded by token services
				details.setDecodedDetails(auth.getDetails());
			}
		}
		auth.setDetails(authentication.getDetails());
		auth.setAuthenticated(true);
		return auth;

	}

	private void checkClientDetails(OAuth2Authentication auth) {
		if (clientDetailsService != null) {
			ClientDetails client;
			try {
				client = clientDetailsService.loadClientByClientId(auth.getOAuth2Request().getClientId());
			}
			catch (ClientRegistrationException e) {
				throw new OAuth2AccessDeniedException("Invalid token contains invalid client id");
			}
			Set<String> allowed = client.getScope();
			for (String scope : auth.getOAuth2Request().getScope()) {
				if (!allowed.contains(scope)) {
					throw new OAuth2AccessDeniedException(
							"Invalid token contains disallowed scope for this client");
				}
			}
		}
	}

}
