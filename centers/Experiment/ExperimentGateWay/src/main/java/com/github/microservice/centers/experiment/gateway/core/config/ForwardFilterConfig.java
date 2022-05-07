package com.github.microservice.centers.experiment.gateway.core.config;

import com.github.microservice.centers.experiment.gateway.core.filters.ForwardFilter;
import com.github.microservice.centers.experiment.gateway.core.helper.ForwardFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Configuration
public class ForwardFilterConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 自定义路由器
     *
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes().route(p -> p.path("/**").filters(f -> f.filter(forwardFilter()).stripPrefix(1)).uri("lb://" + applicationName)).build();
    }

    /**
     * 动态拦截
     *
     * @return
     */
    @Bean
    public ForwardFilter forwardFilter() {
        return new ForwardFilter();
    }


}
