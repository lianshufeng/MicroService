package com.github.microservice.auth.server.core.oauth2.exception;

/**
 * Exception thrown when a client was unable to authenticate.
 *
 * <p>
 * @deprecated See the <a href="https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth 2.0 Migration Guide</a> for Spring Security 5.
 *
 * @author Ryan Heaton
 * @author Dave Syer
 */
@SuppressWarnings("serial")
@Deprecated
public class InvalidClientException extends ClientAuthenticationException {

	public InvalidClientException(String msg) {
		super(msg);
	}

	@Override
	public int getHttpErrorCode() {
		return 401;
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_client";
	}
}
