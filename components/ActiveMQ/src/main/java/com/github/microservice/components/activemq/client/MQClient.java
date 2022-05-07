package com.github.microservice.components.activemq.client;

import com.github.microservice.core.helper.JsonHelper;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MQClient {


    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JsonHelper jsonHelper;


    /**
     * 发送到topic
     */
    public void sendObject(String topic, Object parm) {
        jmsTemplate.convertAndSend(new ActiveMQTopic(topic), this.jsonHelper.toJson(parm));
    }


    /**
     * 发送文本消息
     *
     * @param topic
     * @param text
     */
    public void sendText(String topic, String text) {
        jmsTemplate.convertAndSend(new ActiveMQTopic(topic), text);
    }
}
