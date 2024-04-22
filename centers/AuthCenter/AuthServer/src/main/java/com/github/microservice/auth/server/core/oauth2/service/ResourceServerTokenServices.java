package com.github.microservice.auth.server.core.oauth2.service;

import com.github.microservice.auth.server.core.oauth2.authentication.OAuth2Authentication;
import com.github.microservice.auth.server.core.oauth2.exception.InvalidTokenException;
import com.github.microservice.auth.server.core.oauth2.token.OAuth2AccessToken;
import org.springframework.security.core.AuthenticationException;

/**
 * <p>
 * @deprecated See the <a href="https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth 2.0 Migration Guide</a> for Spring Security 5.
 *
 */
@Deprecated
public interface ResourceServerTokenServices {

	/**
	 * Load the credentials for the specified access token.
	 *
	 * @param accessToken The access token value.
	 * @return The authentication for the access token.
	 * @throws AuthenticationException If the access token is expired
	 * @throws InvalidTokenException if the token isn't valid
	 */
	OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException;

	/**
	 * Retrieve the full access token details from just the value.
	 * 
	 * @param accessToken the token value
	 * @return the full access token with client id etc.
	 */
	OAuth2AccessToken readAccessToken(String accessToken);

}