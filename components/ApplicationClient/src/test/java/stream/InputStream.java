package stream;

import com.github.microservice.core.util.JsonUtil;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import stream.model.User;
import stream.template.InputStreamTemplate;

@EnableBinding(InputStreamTemplate.class)
public class InputStream {


    @StreamListener(InputStreamTemplate.name)
    public void input(Message<User> message) throws Exception {
        System.out.println(Thread.currentThread() + " input : " + JsonUtil.toJson(message.getPayload()));
    }


}
