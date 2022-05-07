package feign.client;

import com.github.microservice.core.util.JsonUtil;
import feign.model.User;
import feign.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserServiceError implements UserService {

    @Override
    public Object user(User user) {
        System.err.println("访问错误 : " + JsonUtil.toJson(user));
        return null;
    }
}
