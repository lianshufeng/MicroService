package com.example.promise.core.custom;

import com.github.microservice.app.stream.message.MessageBusHelper;
import com.github.microservice.app.stream.message.MessageContentConsumer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class UserCustomPromise extends MessageContentConsumer<UserCustomPromise.Message> {

    public static final String Name = "UserCustomPromiseStream";

    @Value("${spring.application.name}")
    private String _appName;


    /**
     * 取出流名
     *
     * @param applicationName
     * @return
     */
    public static String getStreamName(String applicationName) {
        return applicationName + "_" + Name;
    }


    @Autowired
    private void init(MessageBusHelper messageBusHelper) {
        //绑定消息流
        messageBusHelper.bindConsumer(
                Name,
                this,
                Map.of(
                        "group", _appName
                ),
                Map.of(
                        "maxAttempts", 3, //最大重试次数
                        "backOffInitialInterval", 1000, // 每次重试递增秒
                        "backOffMaxInterval", 15000 // 最大秒数
                ),
                Map.of(
                        "enableDlq", true,
                        "dlqName", Name + "-dlq"
                )
        );
    }

    @Override
    public void handleMessage(MessageHeaders messageHeaders, Message message) throws MessagingException {
        System.out.println(message.toString());
        System.out.println("thread : " + Thread.currentThread());
        int a = 1 / 0;

    }


    @Data
    @Builder
    @ToString(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {

        private String name;

        private long time;
    }


}
