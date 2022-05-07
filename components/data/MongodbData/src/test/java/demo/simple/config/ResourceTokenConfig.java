package demo.simple.config;

import com.github.microservice.components.data.mongo.token.config.ResourceTokenConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ResourceTokenConfiguration.class)
public class ResourceTokenConfig {

}
