package demo;

import com.github.microservice.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan("demo.simple")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DemoMongodbApplication extends ApplicationBootSuper {


    public static void main(String[] args) {
        SpringApplication.run(DemoMongodbApplication.class, args);
    }


}
