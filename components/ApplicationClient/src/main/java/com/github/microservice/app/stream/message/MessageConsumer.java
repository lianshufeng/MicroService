package com.github.microservice.app.stream.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;
import java.util.Map;

public abstract class MessageConsumer<T> extends MessageContentConsumer<T> {

    @Autowired
    private MessageBusHelper messageBusHelper;


    private MessageBind bind;

    /**
     * 绑定模型
     *
     * @return
     */
    public abstract MessageBind bind();


    /**
     * 取消订阅
     */
    @PreDestroy
    private void shutdown() {
        this.messageBusHelper.unbindConsumers(
                bind.getTopic()
        );
    }

    @Autowired
    private void init() {
        this.bind = bind();
        this.messageBusHelper.bindConsumer(
                bind.getTopic(),
                this,
                bind.getBindingPropertiesMap(),
                bind.getConsumerPropertiesMap(),
                bind.getExtendedConsumerPropertiesMap()
        );
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    this.shutdown();
                }
        ));
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class MessageBind {
        private String topic;
        private Map<String, Object> bindingPropertiesMap;
        private Map<String, Object> consumerPropertiesMap;
        private Map<String, Object> extendedConsumerPropertiesMap;
    }
}
