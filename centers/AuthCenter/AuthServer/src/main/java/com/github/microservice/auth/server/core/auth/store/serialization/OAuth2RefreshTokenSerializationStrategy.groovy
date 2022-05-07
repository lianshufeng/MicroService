package com.github.microservice.auth.server.core.auth.store.serialization

import com.github.microservice.core.util.JsonUtil
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken
import org.springframework.security.oauth2.common.OAuth2RefreshToken
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy

import java.nio.charset.StandardCharsets

class OAuth2RefreshTokenSerializationStrategy implements RedisTokenStoreSerializationStrategy {
    @Override
    def <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return new DefaultOAuth2RefreshToken(JsonUtil.toObject(new String(bytes, "UTF-8"), String.class));
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
