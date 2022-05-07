package demo;

import com.github.microservice.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("demo.simple")
public class TestJpaApplication extends ApplicationBootSuper {

    public static void main(String[] args) {
        SpringApplication.run(TestJpaApplication.class, args);
    }
}
