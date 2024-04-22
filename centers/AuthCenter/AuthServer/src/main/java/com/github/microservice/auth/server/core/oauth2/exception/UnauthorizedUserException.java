package com.github.microservice.auth.server.core.oauth2.exception;

/**
 * Exception thrown when a user was unable to authenticate.
 *
 * <p>
 * @deprecated See the <a href="https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth 2.0 Migration Guide</a> for Spring Security 5.
 *
 * @author Dave Syer
 */
@SuppressWarnings("serial")
@Deprecated
public class UnauthorizedUserException extends OAuth2Exception {

	public UnauthorizedUserException(String msg, Throwable t) {
		super(msg, t);
	}

	public UnauthorizedUserException(String msg) {
		super(msg);
	}

	@Override
	public int getHttpErrorCode() {
		// The spec says this can be unauthorized
		return 401;
	}

	@Override
	public String getOAuth2ErrorCode() {
		// Not in the spec
		return "unauthorized_user";
	}
}
