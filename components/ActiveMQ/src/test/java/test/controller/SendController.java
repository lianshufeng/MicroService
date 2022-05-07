package test.controller;

import com.github.microservice.components.activemq.client.MQClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;



@RestController
public class SendController {

    @Autowired
    private MQClient mqClient;

    @RequestMapping("send")
    public Object send(String topic, String text) {
        mqClient.sendText(topic, text);
        return Map.of("time", System.currentTimeMillis());
    }


}
