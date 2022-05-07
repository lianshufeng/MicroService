package com.example.mongo.core.config;

import com.github.microservice.components.data.jpa.config.JpaDataConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import(JpaDataConfiguration.class)
@EnableJpaRepositories("com.example.mongo.core.dao")
@MapperScan(basePackages = "com.example.mongo.core.mapper")
public class MysqlConfig {
}
