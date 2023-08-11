//package com.github.microservice.components.data.es.config;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import com.github.microservice.components.data.es.conf.ElasticsearchConfig;
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.TransportAddress;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.convert.support.DefaultConversionService;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
//import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
//import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
//import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//@Configuration
//@ComponentScan("com.github.microservice.components.data.es")
//@EnableElasticsearchRepositories("com.github.microservice.components.data.es.dao")
//public class ElasticsearchConfiguration extends ElasticsearchConfigurationSupport {
//
//
//    @Bean
//    public ElasticsearchConfig elasticsearchConfig() {
//        return new ElasticsearchConfig();
//    }
//
//    @Bean
//    public Client elasticsearchClient() throws UnknownHostException {
//        ElasticsearchConfig elasticsearchConfig = elasticsearchConfig();
//
//        //集群名称
//        Settings settings = Settings.builder().put("cluster.name", elasticsearchConfig.getClusterName()).build();
//        TransportClient client = new PreBuiltTransportClient(settings);
//
//        //集群节点
//        for (String hostItem : elasticsearchConfig.getClusterNodes().split(",")) {
//            String[] hosts = hostItem.split(":");
//            if (hosts.length <= 1) {
//                break;
//            }
//            String host = null;
//            int port = 9300;
//            if (hosts.length > 0) {
//                host = hosts[0];
//            }
//            if (hosts.length > 1) {
//                port = Integer.parseInt(hosts[1]);
//            }
//
//            client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
//        }
//
//
//        return client;
//    }
//
////    @Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
////    public ElasticsearchTemplate elasticsearchTemplate(ElasticsearchConverter elasticsearchConverter) throws UnknownHostException {
////        return new ElasticsearchTemplate(elasticsearchClient(), elasticsearchConverter);
////    }
//
////    @Bean
////    @Override
////    public ElasticsearchConverter elasticsearchEntityMapper(SimpleElasticsearchMappingContext elasticsearchMappingContext, ElasticsearchCustomConversions elasticsearchCustomConversions) {
////        ElasticsearchConverter elasticsearchConverter =  super.elasticsearchEntityMapper(elasticsearchMappingContext, elasticsearchCustomConversions);
////        return elasticsearchConverter;
////    }
////
//
//
//
////    @Bean
////    @Override
////    public EntityMapper entityMapper() {
////        ElasticsearchEntityMapper entityMapper = new ElasticsearchEntityMapper(elasticsearchMappingContext(), new DefaultConversionService());
////        entityMapper.setConversions(elasticsearchCustomConversions());
////        return entityMapper;
////    }
//
//}
