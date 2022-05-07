package test.rev;

import com.github.microservice.components.activemq.constant.MQConstant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Slf4j
@Component
public class RevController {


    //接收topic消息
    @SneakyThrows
    @JmsListener(destination = MQConstant.RequetTopic, containerFactory = MQConstant.TopicListenerContainerFactory)
    public void handlerTopic(Message message) {
        //取出消息id
        String msgId = message.getJMSMessageID().replaceAll(":", "").replaceAll("-", "");
        log.info("msg -> {} ", msgId);
    }

}
