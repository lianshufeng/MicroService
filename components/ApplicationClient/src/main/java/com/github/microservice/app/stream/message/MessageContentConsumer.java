package com.github.microservice.app.stream.message;

import com.github.microservice.core.helper.JsonHelper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class MessageContentConsumer<T> implements MessageHandler {

    @Autowired
    private JsonHelper jsonHelper;

    @Getter(lazy = true)
    private final Type type = getType();

    /**
     * 取泛型类型
     *
     * @return
     */

    private Type getType() {
        return _type(this.getClass());
    }

    private Type _type(Class cls) {
        Type type = cls.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return parameterizedType.getActualTypeArguments()[0];
        }
        if (cls.getSuperclass() != Object.class) {
            return _type(cls.getSuperclass());
        }
        return null;
    }

    @Override
    @SneakyThrows
    public final void handleMessage(Message<?> message) throws MessagingException {
        final Type type = getType();
        final String msg = new String((byte[]) message.getPayload(), "UTF-8");
        final Class typeClass = (Class) type;
        this.handleMessage(message.getHeaders(),
                String.class == typeClass ? (T) msg : (T) this.jsonHelper.toObject(msg, typeClass)
        );
    }


    /**
     * 消息
     *
     * @throws MessagingException
     */
    public abstract void handleMessage(MessageHeaders messageHeaders, T t) throws MessagingException;

}
