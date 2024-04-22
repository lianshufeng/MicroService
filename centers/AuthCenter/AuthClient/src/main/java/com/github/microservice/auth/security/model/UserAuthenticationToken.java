package com.github.microservice.auth.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * spring 权限令牌
 */
public class UserAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    @Setter
    private Object credentials;

    @Getter
    @Setter
    private Object principal;


    public UserAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

}
