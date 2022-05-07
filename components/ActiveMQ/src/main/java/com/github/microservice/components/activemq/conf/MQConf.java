package com.github.microservice.components.activemq.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "mq")
public class MQConf {


    /**
     * 消息的消费者队列
     */
    private int messageThreadPoolCount = 200;


}
