package com.github.microservice.auth.server;

import com.github.microservice.app.annotation.EnableApplicationClient;
import com.github.microservice.auth.server.core.dao.ApplicationClientDao;
import com.github.microservice.auth.server.core.dao.UserDao;
import com.github.microservice.auth.server.core.domain.ApplicationClient;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;


@SpringBootApplication
@EnableApplicationClient
@ComponentScan("com.github.microservice.auth.server.core")
public class AuthServerApplicationAddClientAndUser extends ApplicationBootSuper {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(AuthServerApplicationAddClientAndUser.class, args);

        UserDao userDao = applicationContext.getBean(UserDao.class);
        userDao.save(User.builder().phone("15123241353").passWord(new BCryptPasswordEncoder().encode("xiaofeng")).build());

        ApplicationClientDao applicationClientDao = applicationContext.getBean(ApplicationClientDao.class);
        applicationClientDao.save(ApplicationClient.builder()
                .clientId("test")
                .secret(new BCryptPasswordEncoder().encode("xiaofeng"))
                .authorizedGrantTypes(Set.of("client_credentials", "password", "refresh_token"))
                .build());


        System.exit(0);
    }

}
