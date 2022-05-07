package demo.simple.config;

import com.github.microservice.components.data.mongo.mongo.config.MongoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
// 重要
@EnableMongoRepositories(basePackages = {"demo.simple.dao"})
@Import(MongoConfiguration.class)
public class MongoJpaConfig {
}
