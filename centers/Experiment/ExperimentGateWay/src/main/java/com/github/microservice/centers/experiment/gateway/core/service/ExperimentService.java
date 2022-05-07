package com.github.microservice.centers.experiment.gateway.core.service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

public interface ExperimentService {


    /**
     * 提取用户请求的令牌
     *
     * @return
     */
    String getToken(ServerHttpRequest request);


    /**
     * 通过令牌获取用户id，该结果会被缓存
     *
     * @return
     */
    String token2Uid(String token);


}
