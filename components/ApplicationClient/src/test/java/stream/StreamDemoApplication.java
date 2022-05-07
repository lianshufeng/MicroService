package stream;

import com.github.microservice.app.annotation.EnableApplicationClient;
import com.github.microservice.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 测试代码的入口
 */

//需要使用此注解，且配置 application.properties

@EnableApplicationClient
@ComponentScan("stream")
public class StreamDemoApplication extends ApplicationBootSuper {


    public static void main(String[] args) {
        ApplicationContext ac = SpringApplication.run(StreamDemoApplication.class, args);


    }


}
