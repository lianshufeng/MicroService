package com.github.microservice.auth.server.core.auth.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

@Slf4j
@SuppressWarnings("deprecation")
public class AuthTokenEndpoint extends TokenEndpoint {

    @Autowired
    private AuthHelper authHelper;

    @Override
    @RequestMapping(value = "/oauth/token", method = RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        log.info("oauth/token : {}", parameters);
        ResponseEntity<OAuth2AccessToken> ret = null;
        try {
            this.authHelper.put(parameters);
            ret = super.postAccessToken(principal, parameters);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (ret == null || ret.getBody() == null) {
                this.authHelper.release(null);
            } else {
                this.authHelper.release(ret.getBody());
            }
        }
    }


}
