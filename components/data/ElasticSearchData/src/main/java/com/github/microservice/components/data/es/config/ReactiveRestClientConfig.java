//package com.github.microservice.components.data.es.config;
//
//import com.github.microservice.components.data.es.conf.ElasticsearchConfig;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
//import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
//import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
//
//@Configuration
//public class ReactiveRestClientConfig extends AbstractReactiveElasticsearchConfiguration {
//
//    @Bean
//    public ElasticsearchConfig elasticsearchConfig() {
//        return new ElasticsearchConfig();
//    }
//
//    @Override
//    @Bean
//    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
//        final ElasticsearchConfig elasticsearchConfig = elasticsearchConfig();
//        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo(elasticsearchConfig.getClusterNodes()) //
//                .build();
//        return ReactiveRestClients.create(clientConfiguration);
//
//    }
//}