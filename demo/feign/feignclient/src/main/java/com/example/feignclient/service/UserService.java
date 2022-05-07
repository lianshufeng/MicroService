package com.example.feignclient.service;

import com.example.feignclient.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "feignserver/user")
public interface UserService {

    @RequestMapping("put")
    Object put(@RequestBody User user);


}
