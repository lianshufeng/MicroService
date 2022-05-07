package com.example.mongo.core.service;

import com.example.feignclient.model.User;
import com.example.feignclient.service.UserService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Override
    @SneakyThrows
    public Object put(User user) {
//        Thread.sleep(1000);
//        throw  new RuntimeException("ss");
        return Map.of(
                "time", System.currentTimeMillis(),
                "user", user
        );
    }
}
