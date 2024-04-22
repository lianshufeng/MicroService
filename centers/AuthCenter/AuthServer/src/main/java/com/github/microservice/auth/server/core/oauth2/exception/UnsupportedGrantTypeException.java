package com.github.microservice.auth.server.core.oauth2.exception;

/**
 * <p>
 * @deprecated See the <a href="https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth 2.0 Migration Guide</a> for Spring Security 5.
 *
 * @author Ryan Heaton
 */
@SuppressWarnings("serial")
@Deprecated
public class UnsupportedGrantTypeException extends OAuth2Exception {

  public UnsupportedGrantTypeException(String msg, Throwable t) {
    super(msg, t);
  }

  public UnsupportedGrantTypeException(String msg) {
    super(msg);
  }

  @Override
  public String getOAuth2ErrorCode() {
    return "unsupported_grant_type";
  }
}