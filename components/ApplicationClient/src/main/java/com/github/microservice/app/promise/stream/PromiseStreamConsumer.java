package com.github.microservice.app.promise.stream;

import com.github.microservice.app.promise.model.PromiseModel;
import com.github.microservice.app.stream.message.MessageBusHelper;
import com.github.microservice.app.stream.message.MessageContentConsumer;
import com.github.microservice.core.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class PromiseStreamConsumer extends MessageContentConsumer<PromiseModel> {

    @Autowired
    private ApplicationContext applicationContext;

    public static final String Name = "PromiseStream";

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
                getStreamName(_appName),
                this,
                Map.of(
                        "group", _appName
                ),
                Map.of(
                        "maxAttempts", 10, //最大重试次数
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
    @SneakyThrows
    public void handleMessage(MessageHeaders messageHeaders, PromiseModel promiseModel) throws MessagingException {
        log.info("promise : {} ", promiseModel);
        //取出bean
        Object bean = this.applicationContext.getBean(promiseModel.getBeanName());
        if (promiseModel.getParameterTypes() == null) {
            MethodUtils.invokeExactMethod(bean, promiseModel.getMethodName());
        } else if (promiseModel.getParameterTypes() == null && promiseModel.getParameter() != null) {
            MethodUtils.invokeExactMethod(bean, promiseModel.getMethodName(), promiseModel.getParameter());
        } else {
            //转换参数
            ParameterModel parameterModel = convertParameter(promiseModel);

            MethodUtils.invokeExactMethod(
                    bean,
                    promiseModel.getMethodName(),
                    parameterModel.getParameter(),
                    parameterModel.getTyeps()
            );
        }
    }


    /**
     * 转换参数
     *
     * @param promiseModel
     */
    @SneakyThrows
    private ParameterModel convertParameter(PromiseModel promiseModel) {
        final ParameterModel parameterModel = new ParameterModel();

        //字符串类型
        final String[] textType = promiseModel.getParameterTypes();
        List<Class> types = new ArrayList<>();
        List<Object> parameter = new ArrayList<>();

        //todo 需要将map转换为指定类型参数
        for (int i = 0; i < textType.length; i++) {
            Class cls = Class.forName(textType[i]);
            Object obj = promiseModel.getParameter()[i];
            if (cls != obj.getClass()) {
                obj = JsonUtil.toObject(JsonUtil.toJson(obj), cls);
            }
            parameter.add(obj);
            types.add(cls);
        }

        parameterModel.setTyeps(types.toArray(new Class[0]));
        parameterModel.setParameter(parameter.toArray(new Object[0]));
        return parameterModel;


    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ParameterModel {
        private Class[] tyeps;
        private Object[] Parameter;
    }


}
