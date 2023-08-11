package demo.simple.config;

import com.github.microservice.components.data.es.config.RestClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories("demo.simple.dao")
@Import(RestClientConfig.class)
public class ESJpaConfig {
}
