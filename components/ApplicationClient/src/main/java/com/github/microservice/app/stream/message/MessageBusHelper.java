package com.github.microservice.app.stream.message;

import com.github.microservice.core.util.bean.BeanUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.cloud.stream.binder.ConsumerProperties;
import org.springframework.cloud.stream.binder.DefaultBinderFactory;
import org.springframework.cloud.stream.binder.ExtendedPropertiesBinder;
import org.springframework.cloud.stream.binding.BindingService;
import org.springframework.cloud.stream.binding.CompositeMessageChannelConfigurer;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.messaging.DirectWithAttributesChannel;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

@Component
public class MessageBusHelper {

    @Autowired
    private CompositeMessageChannelConfigurer channelConfigurer;

    @Autowired
    private BindingService bindingService;


    public void bindConsumer(final String topic, MessageContentConsumer consumer) {
        this.bindConsumer(topic, consumer, null, null, null);
    }

    public void bindConsumer(final String topic, String group, MessageContentConsumer consumer) {
        this.bindConsumer(topic, consumer, Map.of("group", group), null, null);
    }


    /**
     * 绑定消费者
     *
     * @param topic
     * @param consumer
     */
    @SneakyThrows
    public void bindConsumer(
            final String topic,
            MessageContentConsumer consumer,
            Map<String, Object> bindingPropertiesMap,
            Map<String, Object> consumerPropertiesMap,
            Map<String, Object> extendedConsumerPropertiesMap
    ) {
        DirectWithAttributesChannel subscribableChannel = new DirectWithAttributesChannel();
        subscribableChannel.setComponentName(topic);
        subscribableChannel.setAttribute("type", topic);
        this.channelConfigurer.configureInputChannel(subscribableChannel, topic);
        subscribableChannel.subscribe(consumer);

        //如: group
        BindingProperties bindingProperties = this.bindingService.getBindingServiceProperties().getBindingProperties(topic);
        Optional.ofNullable(bindingPropertiesMap).ifPresent((it) -> {
            BeanUtil.setBean(bindingProperties, it);
        });

        //如： maxAttempts 补偿次数
        ConsumerProperties consumerProperties = this.bindingService.getBindingServiceProperties().getConsumerProperties(topic);
        Optional.ofNullable(consumerPropertiesMap).ifPresent((it) -> {
            BeanUtil.setBean(consumerProperties, it);
        });

        //扩展配置, KafkaConsumerProperties, 如：enableDlq
        if (extendedConsumerPropertiesMap != null) {
            Field binderFactoryField = BindingService.class.getDeclaredField("binderFactory");
            binderFactoryField.setAccessible(true);
            DefaultBinderFactory binderFactory = (DefaultBinderFactory) binderFactoryField.get(bindingService);

            Binder binder = binderFactory.getBinder(null, subscribableChannel.getClass());
            Object extendedConsumerProperties = ((ExtendedPropertiesBinder) binder).getExtendedConsumerProperties(topic);
            BeanUtil.setBean(extendedConsumerProperties, extendedConsumerPropertiesMap);
        }

        this.bindingService.bindConsumer(subscribableChannel, topic);
    }

    /**
     * 取消绑定消费者
     *
     * @param topic
     */
    public void unbindConsumers(final String topic) {
        bindingService.unbindConsumers(topic);
    }

}
