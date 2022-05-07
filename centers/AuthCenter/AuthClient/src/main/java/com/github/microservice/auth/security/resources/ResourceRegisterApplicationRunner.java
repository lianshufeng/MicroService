package com.github.microservice.auth.security.resources;

import com.github.microservice.auth.client.model.AuthResourcesNameModel;
import com.github.microservice.auth.client.service.AuthResourcesNameService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class ResourceRegisterApplicationRunner implements ApplicationRunner {

    private static final long sleepTime = 1000L * 60 * 6;

    @Autowired
    private ApplicationContext applicationContext;


    @Autowired
    private AuthResourcesNameService authResourcesNameService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Map<String, ResourceRegister> resourceRegisterMap = this.applicationContext.getBeansOfType(ResourceRegister.class);
        if (resourceRegisterMap == null) {
            return;
        }

        //注册调度器
        final Timer timer = new Timer();
        //注册销毁钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            timer.cancel();
        }));

        //注册调度器
        resourceRegisterMap.values().forEach((it) -> {
            Collection<AuthResourcesNameModel> authResourcesNameModels = it.authResources();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    task(authResourcesNameModels);
                }
            }, 0, sleepTime);
        });


    }


    @SneakyThrows
    private void task(final Collection<AuthResourcesNameModel> authResourcesNameModels) {
        authResourcesNameService.update(authResourcesNameModels.toArray(new AuthResourcesNameModel[0]));
    }


}
