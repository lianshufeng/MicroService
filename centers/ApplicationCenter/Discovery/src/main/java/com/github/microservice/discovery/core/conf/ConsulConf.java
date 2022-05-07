package com.github.microservice.discovery.core.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@ConfigurationProperties("consul")
public class ConsulConf {

    // 最大错误次数
    private Integer maxErrorCount = 6;


    //间隔检查时间
    private long fixedDelay = 5000;

}
