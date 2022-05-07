package com.github.microservice.auth.server.core.listen;

import com.github.microservice.auth.client.helper.TokenEventStreamHelper;
import com.github.microservice.auth.client.model.stream.TokenStreamModel;
import com.github.microservice.auth.client.type.AuthEventType;
import com.github.microservice.auth.client.type.AuthStreamType;
import com.github.microservice.auth.server.core.dao.UserTokenDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AccessTokenMessageListener extends MessageListenerAdapter {

    private final static String AccessName = "access:";
    private final static String RefreshName = "refresh:";

    @Autowired
    private UserTokenDao userTokenDao;

    @Autowired
    private TokenEventStreamHelper tokenEventStreamHelper;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody());
        String patternText = new String(pattern);
        if (key.indexOf(AccessName) == 0) {
            log.info("Token Remove {} -> {}", patternText, key);
            removeAccessToken(key.substring(AccessName.length()));
        } else if (key.indexOf(RefreshName) == 0) {
            log.info("Token Remove {} -> {}", patternText, key);
            removeRefreshToken(key.substring(RefreshName.length()));
        }
    }


    /**
     * 删除访问令牌事件
     *
     * @param key
     */
    private void removeAccessToken(String key) {
        if (resourceToken(key)) {
            this.tokenEventStreamHelper.publish(AuthStreamType.Token, new TokenStreamModel(AuthEventType.Remove, key));
        }
    }


    /**
     * 删除刷新令牌事件
     *
     * @param key
     */
    private void removeRefreshToken(String key) {
        if (resourceToken(key)) {
            userTokenDao.removeByRefreshToken(key);
        }
    }


    /**
     * 原子性的资源锁
     *
     * @param key
     * @return
     */
    private boolean resourceToken(String key) {
        final String tokenKey = "ResourceToken:" + key;
        boolean success = this.redisTemplate.opsForValue().increment(tokenKey, 1) == 1;
        this.redisTemplate.expire(tokenKey, 1, TimeUnit.HOURS);
        return success;
    }


}
