package com.github.microservice.app.core.config;

import com.github.microservice.app.promise.hystrix.PromiseHystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan({
        "com.github.microservice.app.promise",
})
public class PromiseConfig {


    @Autowired
    private void init(ApplicationContext applicationContext, PromiseHystrixCommandExecutionHook promiseHystrixCommandExecutionHook) {
        HystrixConcurrencyStrategy existingConcurrencyStrategy = HystrixPlugins.getInstance().getConcurrencyStrategy();
        HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
        HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
        HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy();
//        HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();
        // reset the Hystrix plugin
        HystrixPlugins.reset();
        // configure the  plugin
        HystrixPlugins.getInstance().registerConcurrencyStrategy(existingConcurrencyStrategy);
        HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
        HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
        HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
        HystrixPlugins.getInstance().registerCommandExecutionHook(promiseHystrixCommandExecutionHook);
        log.info("Context propagation enabled for Hystrix.");

    }
}
