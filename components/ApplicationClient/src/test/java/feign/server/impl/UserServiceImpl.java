package feign.server.impl;

import feign.model.User;
import feign.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Object user(User user) {
        return Map.of(
                "time", System.currentTimeMillis(),
                "user", user
        );
    }
}
