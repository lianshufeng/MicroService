package com.github.microservice.centers.experiment.gateway.core.helper;

import com.github.microservice.centers.experiment.gateway.core.conf.ForwardConf;
import com.github.microservice.centers.experiment.gateway.core.model.ExperimentModel;
import com.github.microservice.centers.experiment.gateway.core.service.ExperimentManagerService;
import com.github.microservice.centers.experiment.gateway.core.service.ExperimentService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@EnableCaching
@Component
public class ForwardFilterHelper {

    //重要： 必须通过me执行方法才能触发缓存
    @Autowired
    private ForwardFilterHelper me;

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private ExperimentManagerService experimentManagerService;

    @Autowired
    private ForwardConf forwardConf;

    private ExecutorService threadPool;

    @Autowired
    private void initThreadPool(ApplicationContext applicationContext) {
        threadPool = Executors.newFixedThreadPool(forwardConf.getMaxThreadPoolCount());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            threadPool.shutdownNow();
        }));
    }


    /**
     * 重新定位请求的转换
     *
     * @param exchange
     * @param chain
     * @return
     */
    @SneakyThrows
    public Mono<Void> forward(ServerWebExchange exchange, GatewayFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final ServerHttpResponse response = exchange.getResponse();

        //取出原业务名
        final String originalServiceName = getOriginalServiceName(exchange);

        //同步执行任务，取出灰度环境中的业务名
        String experimentServiceName = this.executeSyncTask(() -> {
            final String token = this.experimentService.getToken(request);
            if (!StringUtils.hasText(token)) {
                return null;
            }

            //取出token对应的用户id
            final String uid = me.token2Uid(token);
            if (!StringUtils.hasText(uid)) {
                return null;
            }

            //取出用户是否需要使用灰度环境
            return me.queryByUidAndServiceName(uid, originalServiceName);
        });


        return this.changeRequestUrl(exchange, chain, StringUtils.hasText(experimentServiceName) ? experimentServiceName : originalServiceName);
    }


    /**
     * 执行同步任务::
     *
     * @param consumer
     * @param <T>
     * @return
     */
    @SneakyThrows
    private <T> T executeSyncTask(final Consumer<T> consumer) {
        final AtomicReference<T> atomicReference = new AtomicReference<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        threadPool.execute(() -> {
            try {
                atomicReference.set(consumer.execute());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                countDownLatch.countDown();
            }
        });

        try {
            //等待线程执行完成
            countDownLatch.await(forwardConf.getMaxExecuteTimeOut(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return atomicReference.get();
    }


    /**
     * 更改请求的URL
     *
     * @param exchange
     * @param chain
     * @param serviceName
     * @return
     */
    @SneakyThrows
    private Mono<Void> changeRequestUrl(ServerWebExchange exchange, GatewayFilterChain chain, String serviceName) {
        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, new URI(String.format("lb://%s", serviceName)));
        return chain.filter(exchange);
    }


    /**
     * 取出原请求地址
     *
     * @param exchange
     * @return
     */
    private String getOriginalServiceName(ServerWebExchange exchange) {
        String url = getOriginalUrl(exchange, 0);
        int at = url.indexOf("://");
        //取出:// 后面的参数
        String uri = at > -1 ? url.substring(at + 3, url.length()) : url;
        String[] path = uri.split("/");
        return path.length > 1 ? path[1] : null;
    }


    /**
     * 取出原请求的URL
     *
     * @param exchange
     * @param index
     * @return
     */
    private String getOriginalUrl(ServerWebExchange exchange, int index) {
        AtomicReference<String> url = new AtomicReference<>();
        Optional.ofNullable(exchange.getAttributes().get(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR)).ifPresent(it -> {
            if (it instanceof LinkedHashSet) {
                LinkedHashSet set = (LinkedHashSet) it;
                if (set.size() > index) {
                    url.set(String.valueOf(set.toArray()[index]));
                }
            }
        });
        return url.get();
    }


    @Cacheable(cacheNames = "token_uid_cache", key = "#token")
    public String token2Uid(final String token) {
        log.info("token cache miss : {}", token);
        return this.experimentService.token2Uid(token);
    }

    @Cacheable(cacheNames = "uid_service_cache", key = "#uid+'_'+#serviceName")
    public String queryByUidAndServiceName(String uid, String serviceName) {
        log.info("serviceName cache miss : {} - {}", uid, serviceName);
        //业务名转小写
        final String serviceNameLower = serviceName.toLowerCase();
        ExperimentModel[] experimentModels = this.experimentManagerService.queryByUidAndServiceName(uid, serviceNameLower);
        //取出第一个业务并忽略业务名的大小写(全部转到小写)
        final AtomicReference<String> atomicReference = new AtomicReference<>();
        if (experimentModels != null && experimentModels.length > 0) {
            experimentModels[0].getMapping().entrySet().stream().filter((kv) -> {
                return serviceNameLower.equals(kv.getKey().toLowerCase());
            }).findFirst().ifPresent((entry) -> {
                atomicReference.set(entry.getValue());
            });
        }
        return atomicReference.get();
    }


    @FunctionalInterface
    private static interface Consumer<R> {

        R execute();

    }


}
