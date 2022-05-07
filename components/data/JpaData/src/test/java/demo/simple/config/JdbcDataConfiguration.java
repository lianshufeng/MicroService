package demo.simple.config;

import com.github.microservice.components.data.jpa.config.JpaDataConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import(JpaDataConfiguration.class)
@EntityScan("demo.simple.domain")
@EnableJpaRepositories("demo.simple.dao")
public class JdbcDataConfiguration {
}
