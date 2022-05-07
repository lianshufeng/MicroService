package com.github.microservice.auth.server.core.auth.store;

import com.github.microservice.auth.server.core.auth.store.serialization.OAuth2AuthenticationSerializationStrategy;
import com.github.microservice.auth.server.core.auth.store.serialization.OAuth2RefreshTokenSerializationStrategy;
import com.github.microservice.core.helper.SpringBeanHelper;
import com.github.microservice.core.util.JsonUtil;
import com.google.common.base.Preconditions;
import groovy.transform.Undefined;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("deprecation")
public class TokenStoreSerializationStrategy implements RedisTokenStoreSerializationStrategy {

    @Autowired
    private SpringBeanHelper springBeanHelper;

    //序列化工具
    private JdkSerializationStrategy jdkSerializationStrategy = new JdkSerializationStrategy();

    //使用java序列化与反序列化
    private Map<Class, RedisTokenStoreSerializationStrategy> jdkSerializeVector = new ConcurrentHashMap<>();


    @SneakyThrows
    private <T> T injection(Class<? extends RedisTokenStoreSerializationStrategy> clazz) {
        Object o = clazz.newInstance();
        springBeanHelper.injection(o);
        return (T) o;
    }

    @Autowired
    private void init(ApplicationContext applicationContext) {
        jdkSerializeVector.put(OAuth2Authentication.class, injection(OAuth2AuthenticationSerializationStrategy.class));
        jdkSerializeVector.put(OAuth2RefreshToken.class, injection(OAuth2RefreshTokenSerializationStrategy.class));
    }


    @Override
    @SneakyThrows
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        //过滤jdk序列化对象
        if (jdkSerializeVector.containsKey(clazz)) {
            return jdkSerializeVector.get(clazz).deserialize(bytes, clazz);
        }

        Preconditions.checkArgument(clazz != null,
                "clazz can't be null");
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return JsonUtil.toObject(deserializeString(bytes), clazz);
    }

    @Override
    @SneakyThrows
    public String deserializeString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return new String(bytes, "UTF-8");
    }

    @Override
    public byte[] serialize(Object object) {
        if (jdkSerializeVector.containsKey(object.getClass())) {
            return jdkSerializeVector.get(object.getClass()).serialize(object);
        }
        if (object == null) {
            return new byte[0];
        }
        return JsonUtil.toJson(object).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] serialize(String data) {
        if (data == null) {
            return new byte[0];
        }
        return data.getBytes(StandardCharsets.UTF_8);
    }
}
