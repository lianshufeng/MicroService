package com.github.microservice.centers.experiment.gateway.core.filters;

import com.github.microservice.centers.experiment.gateway.core.helper.ForwardFilterHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ForwardFilter implements GatewayFilter, Ordered {


    @Autowired
    private ForwardFilterHelper forwardFilterHelper;


    @Override
    public int getOrder() {
        return RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER + 1;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return forwardFilterHelper.forward(exchange, chain);
    }
}