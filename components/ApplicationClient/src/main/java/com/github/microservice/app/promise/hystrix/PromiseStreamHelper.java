package com.github.microservice.app.promise.hystrix;

import com.github.microservice.app.promise.model.PromiseModel;
import com.github.microservice.app.promise.stream.PromiseStreamConsumer;
import com.github.microservice.app.stream.StreamHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PromiseStreamHelper {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private StreamHelper streamHelper;


    /**
     * 添加
     *
     * @param promiseModel
     */
    public void addFallback(PromiseModel promiseModel) {
        try {
            this.streamHelper.send(PromiseStreamConsumer.getStreamName(applicationName), promiseModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
