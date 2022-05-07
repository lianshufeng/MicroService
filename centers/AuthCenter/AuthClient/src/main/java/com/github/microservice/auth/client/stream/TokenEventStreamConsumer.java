package com.github.microservice.auth.client.stream;

import com.github.microservice.app.stream.message.MessageBusHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 权限事件的流
 */

@Component
public class TokenEventStreamConsumer extends SuperEventStreamConsumer {


    @Autowired
    private void initMessageRegister(MessageBusHelper messageBusHelper) {
        //绑定消息流
        messageBusHelper.bindConsumer(
                topic(), this, Map.of("group", ""), null, null
        );
    }


    @Override
    public String topic() {
        return "TokenEventStream";
    }
}
