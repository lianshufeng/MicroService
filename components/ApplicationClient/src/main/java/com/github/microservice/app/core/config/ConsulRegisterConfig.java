package com.github.microservice.app.core.config;

import com.ecwid.consul.v1.ConsulClient;
import com.github.microservice.app.helper.ConsulHelper;
import com.github.microservice.app.work.ConsulRegisterWork;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.consul.ConsulAutoConfiguration;
import org.springframework.cloud.consul.ConsulProperties;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulManagementRegistrationCustomizer;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class ConsulRegisterConfig {

    @Value("${server.port}")
    private int port;

    @Value("$(spring.cloud.consul.discovery.preferIpAddress)")
    private String preferIpAddress;


    @Bean
    public ConsulHelper consulHelper() {
        return new ConsulHelper();
    }


    @Autowired
    private ConsulProperties consulProperties;


    /**
     * 构建客户端
     *
     * @return
     */
    @Bean
    public ConsulClient consulClient() {
        //取出最佳的主机
        ConsulHelper.HostItem hostItem = consulHelper().getPreferredHost(consulProperties);
        Optional.ofNullable(hostItem.getHost()).ifPresent((host) -> {
            consulProperties.setHost(host);
        });
        Optional.ofNullable(hostItem.getPort()).ifPresent((port) -> {
            consulProperties.setPort(port);
        });
        return ConsulAutoConfiguration.createConsulClient(consulProperties);
    }


    @Bean
    @SneakyThrows
    public ConsulAutoRegistration consulRegistration(AutoServiceRegistrationProperties autoServiceRegistrationProperties, ConsulDiscoveryProperties properties, ApplicationContext applicationContext, ObjectProvider<List<ConsulRegistrationCustomizer>> registrationCustomizers, ObjectProvider<List<ConsulManagementRegistrationCustomizer>> managementRegistrationCustomizers, HeartbeatProperties heartbeatProperties) {
        // 健康检查失败多长时间，取消注册
        if (!StringUtils.hasText(properties.getHealthCheckCriticalTimeout())) {
            properties.setHealthCheckCriticalTimeout("15s");
        }

        // 实力id
        properties.setInstanceId(consulHelper().getInstance());


        //如果当前系统中存在配置文件的固定的ip地址则取系统ip地址
        // 如果配置为 prefer-ip-address: true 则不需要自定义ip
        if (!properties.isPreferIpAddress()) {
            Optional.ofNullable(consulHelper().getHostName()).ifPresent((hostName) -> {
                properties.setHostname(hostName);
            });
        }
        Registration registration = Registration.builder().autoServiceRegistrationProperties(autoServiceRegistrationProperties).properties(properties).context(applicationContext).registrationCustomizers(registrationCustomizers.getIfAvailable()).managementRegistrationCustomizers(managementRegistrationCustomizers.getIfAvailable()).heartbeatProperties(heartbeatProperties).build();
        return registration.execute();
    }

    @Bean
    public ConsulRegisterWork consulRegisterWork() {
        return new ConsulRegisterWork();
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    private static class Registration {
        private AutoServiceRegistrationProperties autoServiceRegistrationProperties;
        private ConsulDiscoveryProperties properties;
        private ApplicationContext context;
        List<ConsulRegistrationCustomizer> registrationCustomizers;
        private List<ConsulManagementRegistrationCustomizer> managementRegistrationCustomizers;
        private HeartbeatProperties heartbeatProperties;


        /**
         * 执行注册
         */
        public ConsulAutoRegistration execute() {
            return ConsulAutoRegistration.registration(this.autoServiceRegistrationProperties, this.properties, this.context, this.registrationCustomizers, this.managementRegistrationCustomizers, this.heartbeatProperties);
        }


    }


}
