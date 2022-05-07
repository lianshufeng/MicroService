package com.github.microservice.discovery.core.timer;


import com.github.microservice.discovery.core.conf.ConsulConf;
import com.github.microservice.discovery.core.helper.ConsulDiscoverHelper;
import com.github.microservice.discovery.core.model.ServiceHealth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CheckTimer {

    @Autowired
    private ConsulDiscoverHelper consulHelper;

    @Autowired
    private ConsulConf consulConf;


    //错误
    private Map<String, Integer> error = new ConcurrentHashMap<>();


    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

    @Autowired
    private void init(ApplicationContext applicationContext) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdownNow();
        }));

        //开始检查
        sleepCheck();
    }

    /**
     * 延迟检查
     */
    private void sleepCheck() {
        executorService.schedule(() -> {
            check();
        }, consulConf.getFixedDelay(), TimeUnit.MILLISECONDS);
    }


    public void check() {
        try {
            Map<String, ServiceHealth> serviceHealths = consulHelper.getCritical();
            if (serviceHealths != null && serviceHealths.size() > 0) {
                //记录
                record(serviceHealths);

                //恢复
                recovery(serviceHealths);

                //检查服务
                checkTimoutService();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        sleepCheck();
    }

    /**
     * 记录错误次数
     */
    private void record(Map<String, ServiceHealth> serviceHealths) {
        for (ServiceHealth serviceHealth : serviceHealths.values()) {
            String serviceId = serviceHealth.getServiceID();
            int count = this.error.getOrDefault(serviceHealth.getServiceID(), 0) + 1;
            error.put(serviceId, count);
        }
    }

    /**
     * 恢复数据
     *
     * @param serviceHealths
     */
    private void recovery(Map<String, ServiceHealth> serviceHealths) {
        Set<String> removeKeys = new HashSet<>();
        for (String key : this.error.keySet()) {
            if (!serviceHealths.containsKey(key)) {
                removeKeys.add(key);
            }
        }
        removeKeys.forEach((key) -> {
            this.error.remove(key);
        });
    }

    /**
     * 删除超时的service
     */
    private void checkTimoutService() {
        Set<String> removeKeys = new HashSet<>();
        for (Map.Entry<String, Integer> entry : this.error.entrySet()) {
            if (entry.getValue() >= consulConf.getMaxErrorCount()) {
                deleteService(entry.getKey());
                removeKeys.add(entry.getKey());
            }
        }
        removeKeys.forEach((key) -> {
            error.remove(key);
        });
    }

    /**
     * 删除服务名
     *
     * @param serviceId
     */
    private void deleteService(String serviceId) {
        this.consulHelper.deleteService(serviceId);
        log.info("remove : {}  ", serviceId);
    }


}
