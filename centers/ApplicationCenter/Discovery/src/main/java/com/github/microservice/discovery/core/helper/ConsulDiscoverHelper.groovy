package com.github.microservice.discovery.core.helper

import com.github.microservice.core.util.JsonUtil
import com.github.microservice.discovery.core.model.ServiceHealth
import groovy.util.logging.Log
import org.springframework.stereotype.Component

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Log
@Component
class ConsulDiscoverHelper extends HostHelper {

    String url(String uri) {
        return getUrl() + "/" + uri
    }

    /**
     * 获取有异常的数据
     * @return
     */
    Map<String, ServiceHealth> getCritical() {

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create(url("v1/health/state/critical")))
                .build()
        HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        def critical = JsonUtil.toObject(response.body(), Object.class)
        def ret = [:]
        for (def item : critical) {
            ServiceHealth serviceHealth = [
                    'Node'       : item.Node,
                    'CheckID'    : item.CheckID,
                    'Status'     : item.Status,
                    'Output'     : item.Output,
                    'ServiceID'  : item.ServiceID,
                    'ServiceName': item.ServiceName
            ] as ServiceHealth
            ret[serviceHealth.ServiceID] = serviceHealth
        }
        return ret;
    }


    /**
     * 删除业务
     * @param serviceId
     * @return
     */
    void deleteService(String serviceId) {
        getUrls().parallelStream().forEach((hostUrl) -> {
            String url = String.format("%s/v1/agent/service/deregister/%s", hostUrl, serviceId);
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(url))
                    .PUT(HttpRequest.BodyPublishers.ofString(""))
                    .build()
            httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        })
    }

}
