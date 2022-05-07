package com.github.microservice.auth.client.stream;

import com.github.microservice.app.stream.message.MessageContentConsumer;
import com.github.microservice.auth.client.model.StreamModel;
import com.github.microservice.core.util.os.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class SuperEventStreamConsumer extends MessageContentConsumer<StreamModel> {

    @Autowired
    private ApplicationContext applicationContext;

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(SystemUtil.getCpuCoreCount() * 2);

    @Autowired
    private void initShutdownHook(ApplicationContext applicationContext) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            threadPool.shutdownNow();
        }));
    }


    /**
     * 消费者的订阅
     *
     * @return
     */
    public abstract String topic();


    @Override
    public void handleMessage(MessageHeaders messageHeaders, StreamModel streamModel) throws MessagingException {
        log.info("EventStream({}) : [{}] -- [{}] -> {} - {}", this.getClass().getSimpleName(), streamModel.getType(), streamModel.getParameter().getEventType(), streamModel.getParameter().getId(), streamModel.getParameter().getItems());
        threadPool.execute(() -> {
            try {
                final Class<? extends ApplicationEvent> eventClass = streamModel.getType().getEventClass();
                this.applicationContext.publishEvent(eventClass.getConstructor(Object.class).newInstance(streamModel));
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        });
    }


}
