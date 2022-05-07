package com.github.microservice.auth.server.core.config;

import com.github.microservice.auth.server.core.listen.AccessTokenMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.List;

@Slf4j
@Configuration
public class ListenerConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private AccessTokenMessageListener accessTokenMessageListener;


    @Value("${spring.redis.database}")
    private Integer dbNumber;


    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(redisConnectionFactory);
        listenerContainer.addMessageListener(accessTokenMessageListener, List.of(
                new PatternTopic("__keyevent@" + dbNumber + "__:expired"),
                new PatternTopic("__keyevent@" + dbNumber + "__:del")
        ));
        listenerContainer.setErrorHandler(e -> log.error("There was an error in redis key expiration listener container", e));
        return listenerContainer;
    }


}
