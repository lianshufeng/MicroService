package com.github.microservice.demo.experiment.service1.boot;

import com.github.microservice.app.core.config.ConsulRegisterConfig;
import com.github.microservice.core.runner.BannerApplicationRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@EnableDiscoveryClient
@ComponentScan("com.github.microservice.demo.experiment.service1.core")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({ConsulRegisterConfig.class})
public class ExperimentService1Application {

    public static void main(String[] args) {
        SpringApplication.run(ExperimentService1Application.class, args);
    }


    /**
     * 启动成功后打印 Banner
     *
     * @return
     */
    @Bean
    public ApplicationRunner BannerApplicationRunner() {
        return new BannerApplicationRunner();
    }

}
