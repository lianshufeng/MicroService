package com.github.microservice.components.data.jpa.config;

import com.github.microservice.components.data.base.config.PaginationConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA 配置
 */

//允许自动装载数据源
@Import({DataSourceAutoConfiguration.class, PaginationConfiguration.class})

//允许使用jpa注解
@EnableJpaAuditing

//允许事务管理
@EnableTransactionManagement

@ComponentScan("com.github.microservice.components.data.base.helper")
public class JpaDataConfiguration {


}
