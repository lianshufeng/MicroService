package com.github.microservice.components.data.es.config;

import com.github.microservice.components.data.es.conf.ElasticsearchConfig;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@ComponentScan("com.github.microservice.components.data.es")
@EnableElasticsearchRepositories("com.github.microservice.components.data.es.dao")
public class RestClientConfig extends AbstractElasticsearchConfiguration {

    @Bean
    public ElasticsearchConfig elasticsearchConfig() {
        return new ElasticsearchConfig();
    }

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ElasticsearchConfig elasticsearchConfig = elasticsearchConfig();
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(elasticsearchConfig.getClusterNodes())
                .build();
        return RestClients.create(clientConfiguration).rest();
    }


    //
//    @Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
//    public ElasticsearchTemplate elasticsearchTemplate(ApplicationContext applicationContext)  {
//        final ElasticsearchOperations elasticsearchOperations = applicationContext.getBean(ElasticsearchOperations.class);
//        return null;
//    }


}
