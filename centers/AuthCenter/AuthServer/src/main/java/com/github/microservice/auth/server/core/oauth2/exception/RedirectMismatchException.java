package com.github.microservice.auth.server.core.oauth2.exception;

/**
 * <p>
 * @deprecated See the <a href="https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth 2.0 Migration Guide</a> for Spring Security 5.
 *
 * @author Ryan Heaton
 */
@SuppressWarnings("serial")
@Deprecated
public class RedirectMismatchException extends ClientAuthenticationException {

  public RedirectMismatchException(String msg, Throwable t) {
    super(msg, t);
  }

  public RedirectMismatchException(String msg) {
    super(msg);
  }

  @Override
  public String getOAuth2ErrorCode() {
    return "invalid_grant";
  }
}
