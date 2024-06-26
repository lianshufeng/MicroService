package com.github.microservice.auth.server.core.oauth2.exception;

import com.github.microservice.auth.server.core.oauth2.util.OAuth2Utils;

import java.util.Set;


/**
 * Exception representing an invalid scope in a token or authorization request (i.e. from an Authorization Server). Note
 * that this is not the same as an access denied exception if the scope presented to a Resource Server is insufficient.
 * The spec in this case mandates a 400 status code.
 *
 * <p>
 * @deprecated See the <a href="https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth 2.0 Migration Guide</a> for Spring Security 5.
 *
 * @author Ryan Heaton
 * @author Dave Syer
 */
@SuppressWarnings("serial")
@Deprecated
public class InvalidScopeException extends OAuth2Exception {

	public InvalidScopeException(String msg, Set<String> validScope) {
		this(msg);
		addAdditionalInformation("scope", OAuth2Utils.formatParameterList(validScope));
	}

	public InvalidScopeException(String msg) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_scope";
	}

}