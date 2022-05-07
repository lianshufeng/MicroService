package com.github.microservice.auth.client.helper;


import com.github.microservice.app.stream.StreamHelper;
import com.github.microservice.auth.client.model.StreamModel;
import com.github.microservice.auth.client.model.stream.SuperStreamModel;
import com.github.microservice.auth.client.type.AuthStreamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

/**
 * 权限事件流
 */
@Component
public class AuthEventStreamHelper {

    /**
     * 权限的流的名称
     */
    public static final String AuthEventStreamName = "AuthEventStream";


    @Autowired
    private StreamHelper streamHelper;


    /**
     * 发布事件
     *
     * @param type
     */
    public void publish(SuperStreamModel parameter) {
        final AuthStreamType type = AuthStreamType.findByStreamClass(parameter.getClass());
        Message<?> message = MessageBuilder.withPayload(
                StreamModel
                        .builder()
                        .type(type)
                        .parameter(parameter)
                        .build()
        ).build();
        this.streamHelper.send(AuthEventStreamName, message, MimeTypeUtils.APPLICATION_JSON);
    }

}

