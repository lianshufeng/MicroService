package com.github.microservice.components.activemq.config;

import com.github.microservice.components.activemq.conf.MQConf;
import com.github.microservice.components.activemq.constant.MQConstant;
import jakarta.jms.Destination;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TopicConfiguration {

    @Autowired
    private MQConf mqConfig;


    @Bean
    public Destination requetQueue() {
        return new ActiveMQTopic(MQConstant.RequetTopic);
    }


}
