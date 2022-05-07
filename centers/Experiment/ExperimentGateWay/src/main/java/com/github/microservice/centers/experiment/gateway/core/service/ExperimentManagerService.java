package com.github.microservice.centers.experiment.gateway.core.service;

import com.github.microservice.centers.experiment.gateway.core.conf.ExperimentManagerServiceConf;
import com.github.microservice.centers.experiment.gateway.core.model.ExperimentModel;
import com.github.microservice.core.util.result.InvokerResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ExperimentManagerService {

    @Autowired
    private ExperimentManagerServiceConf experimentManagerServiceConf;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    final static String QueryByUidAndServiceNameUrl = "http://%s/manager/experiment/queryByUidAndServiceName?uid=%s&serviceName=%s";


    @SneakyThrows
    public ExperimentModel[] queryByUidAndServiceName(String uid, String serviceName) {
        String url = String.format(QueryByUidAndServiceNameUrl, experimentManagerServiceConf.getServiceName().toLowerCase(), uid, serviceName);
        Object ret = restTemplate.getForObject(url, InvokerResult.class).getContent();
        if (ret == null) {
            return null;
        }
        //将反序列化的json对象转换为java对象
        return ((List<Map<String, Object>>) ret).stream().map((map) -> {
            ExperimentModel model = new ExperimentModel();
            BeanMap.create(model).putAll(map);
            return model;
        }).toArray(ExperimentModel[]::new);

    }


}
