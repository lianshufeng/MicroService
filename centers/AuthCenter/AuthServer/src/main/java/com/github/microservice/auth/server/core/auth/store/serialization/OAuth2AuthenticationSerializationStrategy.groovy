package com.github.microservice.auth.server.core.auth.store.serialization


import com.github.microservice.auth.server.core.auth.endpoint.AuthHelper
import com.github.microservice.core.util.JsonUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.OAuth2Request
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy

import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

class OAuth2AuthenticationSerializationStrategy implements RedisTokenStoreSerializationStrategy {


    @Autowired
    private AuthHelper authHelper;


    def readValue(Map param, String key) {
        Object value = param.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Collection) {
            return new HashSet<>(value);
        }
        return value;
    }

    @Override
    def <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        Map<String, Object> oAuth2AuthenticationMap = JsonUtil.toObject(new String(bytes, StandardCharsets.UTF_8), Map.class)
        Map<String, Object> oauth2RequestMap = oAuth2AuthenticationMap.get("oauth2Request")
        OAuth2Request oAuth2Request = new OAuth2Request(
                readValue(oauth2RequestMap, "requestParameters"),
                readValue(oauth2RequestMap, "clientId"),
                readValue(oauth2RequestMap, "authorities"),
                readValue(oauth2RequestMap, "approved"),
                readValue(oauth2RequestMap, "scope"),
                readValue(oauth2RequestMap, "resourceIds"),
                readValue(oauth2RequestMap, "redirectUri"),
                readValue(oauth2RequestMap, "responseTypes"),
                readValue(oauth2RequestMap, "extensions")
        )

        Map<String, Object> userAuthenticationMap = oAuth2AuthenticationMap.get("userAuthentication");


        def principal = userAuthenticationMap.get("principal")
        UserDetails userDetails = new User(
                principal['username'],
                principal['password'] == null ? "" : principal['password'],
                principal['enabled'],
                principal['accountNonExpired'],
                principal['credentialsNonExpired'],
                principal['accountNonLocked'],
                ((Collection) userAuthenticationMap.get("authorities")).stream().map((item) -> {
                    return new GrantedAuthority() {
                        @Override
                        String getAuthority() {
                            return item['authority']
                        }
                    }
                }).collect(Collectors.toSet())
        );




        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                userAuthenticationMap.get("credentials"),
                ((Collection) userAuthenticationMap.get("authorities")).stream().map((item) -> {
                    return new GrantedAuthority() {
                        @Override
                        String getAuthority() {
                            return item['authority']
                        }
                    }
                }).collect(Collectors.toSet()) as Collection<? extends GrantedAuthority>
        );


        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authenticationToken)
        return oAuth2Authentication
    }

    @Override
    String deserializeString(byte[] bytes) {
        return null
    }

    @Override
    byte[] serialize(Object object) {
        return JsonUtil.toJson(object).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    byte[] serialize(String data) {
        return new byte[0]
    }
}
