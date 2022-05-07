package com.github.microservice.app.work;

import com.ecwid.consul.v1.ConsulClient;
import com.github.microservice.app.helper.ConsulHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Slf4j
@EnableScheduling
public class ConsulRegisterWork implements ApplicationRunner {

    @Getter
    @Setter
    private boolean autoTryRegistration;


    @Autowired
    private ConsulClient consulClient;

    @Autowired
    private ConsulHelper consulHelper;

    @Autowired
    private ConsulAutoRegistration consulAutoRegistration;

    @Autowired
    private ConsulRegistration consulRegistration;

    @Autowired
    private ConsulServiceRegistry consulServiceRegistry;

    //表示每天8时30分0秒执行
    @Scheduled(fixedRate = 10000)
    public void cronRegisterWorkJob() {
        if (!this.autoTryRegistration) {
            return;
        }

        try {
            registerWork();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SneakyThrows
    private void registerWork() {
        //如果没有注册，则自动注册一个
        if (!consulClient.getAgentServices().getValue().keySet().contains(this.consulHelper.getInstance())) {
            log.info("registerWork : {}", this.consulHelper.getInstance());
            consulServiceRegistry.register(consulRegistration);
        }
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.autoTryRegistration = true;
    }
}
