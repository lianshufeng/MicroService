package com.github.microservice.auth.server.core.service.user;

import com.github.microservice.auth.client.type.LoginType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserManager {


    private Map<LoginType, LocalUserService> cache = new ConcurrentHashMap<>();


    @Autowired
    private void init(ApplicationContext applicationContext) {
        applicationContext.getBeansOfType(LocalUserService.class).values().forEach((it) -> {
            cache.put(it.type(), it);
        });
    }


    /**
     * 取出接口
     *
     * @param type
     * @return
     */
    public LocalUserService get(LoginType type) {
        return this.cache.get(type);
    }

    /**
     * 取出含空的接口
     *
     * @param type
     * @return
     */
    public Optional<LocalUserService> getOptional(LoginType type) {
        return Optional.ofNullable(get(type));
    }

}
