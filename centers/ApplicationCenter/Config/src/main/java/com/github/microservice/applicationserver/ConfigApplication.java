package com.github.microservice.applicationserver;

import com.github.microservice.app.annotation.EnableApplicationClient;
import com.github.microservice.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.config.server.EnableConfigServer;


@EnableConfigServer
@EnableApplicationClient
public class ConfigApplication extends ApplicationBootSuper {

    /**
     * 默认入口方法
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }

}
