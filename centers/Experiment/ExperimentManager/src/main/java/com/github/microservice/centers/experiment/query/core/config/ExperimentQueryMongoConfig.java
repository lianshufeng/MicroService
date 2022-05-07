package com.github.microservice.centers.experiment.query.core.config;

import com.github.microservice.components.data.mongo.mongo.config.MongoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Import(MongoConfiguration.class)
@EnableMongoRepositories("com.github.microservice.centers.experiment.query.core.dao")
public class ExperimentQueryMongoConfig {
}
