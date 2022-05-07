package com.github.microservice.app.stream;

import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;

public class StreamHelper {

    @Autowired
    @Delegate
    private StreamBridge streamBridge;


}
