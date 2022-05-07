package com.github.microservice.centers.experiment.gateway.core.service.impl;

import com.github.microservice.centers.experiment.gateway.core.service.ExperimentService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Slf4j
public class ExperimentServiceImpl implements ExperimentService {


    private final static String AccessTokenName = "accesstoken";

//    @Autowired
//    private WebClient.Builder webClientBuilder;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public String getToken(ServerHttpRequest request) {
        return request.getHeaders().getFirst(AccessTokenName);
    }

    @Override
    @SneakyThrows
    public String token2Uid(String token) {
        //todo  可以通过 restTemplate 访问权限中心，查询token和uid的映射关系
        return token;
    }

}
