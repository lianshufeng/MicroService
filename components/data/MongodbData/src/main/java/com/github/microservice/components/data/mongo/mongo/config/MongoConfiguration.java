package com.github.microservice.components.data.mongo.mongo.config;

import com.github.microservice.components.data.base.config.PaginationConfiguration;
import com.github.microservice.components.data.mongo.mongo.config.converts.BigDecimalToDecimal128Converter;
import com.github.microservice.components.data.mongo.mongo.config.converts.Decimal128ToBigDecimalConverter;
import com.mongodb.ReadPreference;
import org.bson.codecs.DocumentCodec;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

/**
 * 集成自动配置
 */
@Configuration
@EnableMongoAuditing
@ComponentScan({"com.github.microservice.components.data.mongo.mongo","com.github.microservice.components.data.base.helper"})
@EnableMongoRepositories("com.github.microservice.components.data.mongo.mongo.dao")
@Import(PaginationConfiguration.class)
public class MongoConfiguration {


    static {
        //jdk 11 以上，强制使用 TLSv1.3 ,  mongo 驱动目前兼容 TLSv1.2
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
    }

    /**
     * 自定义转换器
     *
     * @param mongoDbFactory
     * @param mongoMappingContext
     * @return
     * @throws Exception
     */
    @Bean
    @ConditionalOnMissingBean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory mongoDbFactory, MongoMappingContext mongoMappingContext) throws Exception {
        DefaultDbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        List<Object> list = new ArrayList<>();
        list.add(new BigDecimalToDecimal128Converter());//自定义的类型转换器
        list.add(new Decimal128ToBigDecimalConverter());//自定义的类型转换器
        converter.setCustomConversions(new MongoCustomConversions(list));
        return converter;
    }

    /**
     * 事务
     *
     * @param dbFactory
     * @return
     */
    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    /**
     * 配置读写分离
     *
     * @return
     */
    @Bean
    public ReadPreference readPreference(MongoTemplate mongoTemplate) {
//        首选主节点，大多情况下读操作在主节点，如果主节点不可用，如故障转移，读操作在从节点。
        ReadPreference readPreference = ReadPreference.primaryPreferred();
        mongoTemplate.setReadPreference(readPreference);
        return readPreference;
    }

    @Bean
    public DocumentCodec documentCodec(MongoDatabaseFactory dbFactory) {
        return new DocumentCodec(dbFactory.getCodecRegistry());
    }


// 	 @EventListener(ApplicationReadyEvent.class)
//	 public void initIndicesAfterStartup() {
//
//	    IndexOperations indexOps = mongoTemplate.indexOps(DomainType.class);
//
//	     IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
//	     resolver.resolveIndexFor(DomainType.class).forEach(indexOps::ensureIndex);
//	 }
}
