package com.github.microservice.components.push.service;

import com.github.microservice.app.stream.StreamHelper;
import com.github.microservice.components.push.model.MqMessage;
import com.github.microservice.components.push.model.SmsMessage;
import com.github.microservice.components.push.type.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PushService {


    private final static String StreamName = "push-stream";

    @Autowired
    private StreamHelper streamHelper;

    /**
     * 发送短信
     * @param message
     */
    public void sendSMS(SmsMessage message){
        streamHelper.send(StreamName, Map.of("messageType", MessageType.Sms,"info",message));
    }


    /**
     * 发送mq消息
     * @param message
     */
    public void sendMq(MqMessage message){
        streamHelper.send(StreamName, Map.of("messageType", MessageType.Mq,"info",message));
    }


}
