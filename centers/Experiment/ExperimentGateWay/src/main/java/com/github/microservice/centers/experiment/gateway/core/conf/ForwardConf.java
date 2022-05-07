package com.github.microservice.centers.experiment.gateway.core.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "forward")
public class ForwardConf {

    //最大处理转发的线程池数量
    private int maxThreadPoolCount = 1000;

    //最大执行超时时间
    private long maxExecuteTimeOut = 5000L;


}
