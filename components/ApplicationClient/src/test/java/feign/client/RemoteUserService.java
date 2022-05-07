package feign.client;

import feign.model.User;
import feign.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(name = "applicationclient/user", fallback = UserServiceError.class)
public interface RemoteUserService extends UserService {

    @RequestMapping("test")
    Object user(User user);
}
