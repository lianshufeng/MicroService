package com.github.microservice.auth.server.core.init;

import com.github.microservice.auth.client.content.ResultState;
import com.github.microservice.auth.client.model.EnterpriseModel;
import com.github.microservice.auth.client.model.UserAuthModel;
import com.github.microservice.auth.client.service.EnterpriseService;
import com.github.microservice.auth.client.service.UserService;
import com.github.microservice.auth.server.core.conf.InitConf;
import com.github.microservice.auth.server.core.dao.EnterpriseDao;
import com.github.microservice.auth.server.core.domain.Enterprise;
import com.github.microservice.auth.server.core.service.ApplicationClientService;
import com.github.microservice.core.conf.ApplicationConfig;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 初始化权限中心
 */
@Component
public class InitCompose implements ApplicationRunner {

    @Autowired
    private InitConf initConf;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationClientService applicationClientService;

    @Autowired
    private EnterpriseService enterpriseService;




    /**
     * 注册企业
     */
    @SneakyThrows
    private void initEnterprise() {
        Optional.ofNullable(initConf.getEnterprise()).ifPresent((enterprise) -> {
            for (InitConf.Enterprise ep : enterprise) {
                if (!(this.enterpriseService.existsByName(ep.getName()).getState() == ResultState.Success)) {
                    EnterpriseModel epModel = new EnterpriseModel();
                    BeanUtils.copyProperties(ep, epModel);
                    this.enterpriseService.add(epModel);
                }
            }
        });
    }


    @SneakyThrows
    private void initApplicationClient() {
        Optional.ofNullable(initConf.getClient()).ifPresent((items) -> {
            for (InitConf.ApplicationClient client : items) {
                this.applicationClientService.add(client.getClientId(), client.getSecret(), client.getAuthorizedGrantTypes());
            }
        });

    }

    @SneakyThrows
    private void initUser() {
        Optional.ofNullable(initConf.getUser()).ifPresent((items) -> {
            for (InitConf.User user : items) {
                userService.add(UserAuthModel
                        .builder()
                        .loginType(user.getLoginType())
                        .loginValue(user.getLoginValue())
                        .passWord(user.getPassWord())
                        .build());
            }
        });
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        initApplicationClient();
        initUser();
        initEnterprise();
    }
}
