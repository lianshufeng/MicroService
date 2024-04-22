package com.github.microservice.auth.server.core.oauth2.exception;

import com.github.microservice.auth.server.core.oauth2.util.OAuth2Utils;
import org.springframework.security.access.AccessDeniedException;

import java.util.Set;

/**
 * Exception representing insufficient scope in a token when a request is handled by a Resource Server. It is akin to an
 * {@link AccessDeniedException} and should result in a 403 (FORBIDDEN) HTTP status.
 *
 * <p>
 * @deprecated See the <a href="https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth 2.0 Migration Guide</a> for Spring Security 5.
 *
 * @author Dave Syer
 */
@SuppressWarnings("serial")
@Deprecated
public class InsufficientScopeException extends OAuth2Exception {

	public InsufficientScopeException(String msg, Set<String> validScope) {
		this(msg);
		addAdditionalInformation("scope", OAuth2Utils.formatParameterList(validScope));
	}

	public InsufficientScopeException(String msg) {
		super(msg);
	}

	@Override
	public int getHttpErrorCode() {
		return 403;
	}

	@Override
	public String getOAuth2ErrorCode() {
		// Not defined in the spec, so not really an OAuth2Exception
		return "insufficient_scope";
	}

}