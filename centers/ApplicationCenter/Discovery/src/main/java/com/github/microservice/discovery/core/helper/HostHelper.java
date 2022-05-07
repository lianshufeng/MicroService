package com.github.microservice.discovery.core.helper;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public abstract class HostHelper {

    @Value("${spring.cloud.consul.host}")
    private String consulHost;

    @Value("${spring.cloud.consul.port}")
    private String consulPort;

    @Getter
    private HostItem[] hostItems;


    //URL计数器
    private AtomicInteger atomicUrlCount = new AtomicInteger();


    @Autowired
    private void init(ApplicationContext applicationContext) {
        //多个ip则进行分割
        this.hostItems = Set.of(consulHost.split(","))
                .stream()
                .map((it) -> {
                    String[] hostSplit = it.split(":");
                    if (hostSplit.length == 0) {
                        return HostItem.builder().host(hostSplit[0]).build();
                    } else if (hostSplit.length == 1) {
                        return HostItem.builder().host(hostSplit[0]).port(Integer.parseInt(consulPort)).build();
                    } else if (hostSplit.length >= 1) {
                        return HostItem.builder().host(hostSplit[0]).port(Integer.parseInt(hostSplit[1])).build();
                    }
                    return null;
                }).filter(it -> it != null)
                .toArray(HostItem[]::new);
    }

    /**
     * 获取所有的url
     *
     * @return
     */
    public synchronized Set<String> getUrls() {
        return Arrays.stream(hostItems).map(it -> {
            return "http://" + it.getHost() + ":" + it.getPort();
        }).collect(Collectors.toSet());
    }


    /**
     * 取出URL
     *
     * @return
     */
    public synchronized String getUrl() {
        int index = atomicUrlCount.addAndGet(1);
        if (index < 0) {
            atomicUrlCount.set(0);
            index = 0;
        }
        final HostItem hostItem = hostItems[(index % hostItems.length)];
        return "http://" + hostItem.getHost() + ":" + hostItem.getPort();
    }


    @Data
    @NoArgsConstructor
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class HostService extends HostItem {

        private Integer serviceCount;

    }


    @Data
    @Builder
    @NoArgsConstructor
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class HostItem {
        private String host;
        private Integer port;
    }
}
