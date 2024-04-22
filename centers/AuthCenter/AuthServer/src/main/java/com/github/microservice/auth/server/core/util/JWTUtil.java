package com.github.microservice.auth.server.core.util;

import com.google.common.primitives.Bytes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;


public class JWTUtil {


    //默认的密钥
    private final static Key DefaultSecretKey = Keys.hmacShaKeyFor(Bytes.concat(DigestUtils.md5Digest("AuthCenter".getBytes(StandardCharsets.UTF_8)), DigestUtils.md5Digest("AuthCenter_xf".getBytes(StandardCharsets.UTF_8))));


    /**
     * 生成jwt
     *
     * @param jwtModel
     * @param SecretKey
     * @return
     */
    public static String generateJWT(JwtModel jwtModel, Key SecretKey) {
        return Jwts.builder()
                .subject(jwtModel.getSubject()) //主体
                .claims(jwtModel.getClaims()) //扩展数据
                .expiration(jwtModel.getExpiration()) //过期时间
                .signWith(SecretKey).compact();
    }


    /**
     * 转换jwt模型
     *
     * @param jwt
     * @param SecretKey
     * @return
     */
    public static JwtModel parserJWT(String jwt, Key SecretKey) {
        final Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SecretKey).build().parseClaimsJws(jwt);
        final Claims claims = claimsJws.getPayload();
        return JwtModel.builder()
                .subject(claims.getSubject())
                .claims(claims)
                .expiration(claims.getExpiration())
                .build();
    }


//    @SneakyThrows
//    public static void main(String[] args) {
//        String jwt = generateJWT(JwtModel.builder()
//                .subject(UUID.randomUUID().toString())
//                .claims(Map.of("x", "b", "c", 3))
//                .expiration(new Date(System.currentTimeMillis() + System.currentTimeMillis() + 1))
//                .build(), DefaultSecretKey);
//        System.out.println(jwt);
//
//        JwtModel jwtModel = parserJWT(jwt, DefaultSecretKey);
//        System.out.println(jwtModel);
//    }


    @Data
    @Builder
    @Accessors(chain = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JwtModel {

        private String subject;

        private Map<String, Object> claims;

        private Date expiration;

    }

}




