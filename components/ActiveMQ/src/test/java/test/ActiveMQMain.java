package test;

import com.github.microservice.components.activemq.config.MQConfig;
import com.github.microservice.core.mvc.MVCConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({MQConfig.class, MVCConfiguration.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ActiveMQMain {
    public static void main(String[] args) {
        SpringApplication.run(ActiveMQMain.class, args);
    }
}
