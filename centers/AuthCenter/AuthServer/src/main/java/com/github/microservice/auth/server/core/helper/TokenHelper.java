package com.github.microservice.auth.server.core.helper;

import com.github.microservice.auth.server.core.conf.AuthConf;
import com.github.microservice.auth.server.core.util.JWTUtil;
import com.google.common.primitives.Bytes;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class TokenHelper {

    @Autowired
    private AuthConf authConf;
    private Key secretKey = null;


    @Autowired
    private void init(ApplicationContext applicationContext) {
        byte[] bin =
                Bytes.concat(
                        DigestUtils.md5Digest(authConf.getJwtKey().getBytes()),
                        DigestUtils.md5Digest(authConf.getJwtKey().getBytes())
                );
        secretKey = Keys.hmacShaKeyFor(bin);
    }


    @SneakyThrows
    public String create(String uid, Map<String, Object> claims, Long expirationTime) {
        return JWTUtil.generateJWT(JWTUtil.JwtModel.builder().subject(uid).claims(claims).expiration(new Date(System.currentTimeMillis() + expirationTime)).build(), this.secretKey);
    }

    /**
     * 过期或者密钥错误
     *
     * @param jwt
     * @return
     */
    public JWTUtil.JwtModel parser(String jwt) {
        try {
            return JWTUtil.parserJWT(jwt, this.secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
