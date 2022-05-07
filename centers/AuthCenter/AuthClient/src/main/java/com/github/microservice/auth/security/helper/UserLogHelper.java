package com.github.microservice.auth.security.helper;

import com.github.microservice.app.stream.StreamHelper;
import com.github.microservice.auth.security.model.AuthDetails;
import com.github.microservice.auth.security.model.UserLogModel;
import com.github.microservice.core.util.net.IPUtil;
import com.github.microservice.core.util.os.SystemUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class UserLogHelper {

    public final static String UserLogStreamName = "UserLogStream";

    private ThreadLocal<LogItems> threadLocal = new ThreadLocal<>();

    @Autowired
    private StreamHelper streamHelper;

    @Autowired
    private AuthHelper authHelper;


    @Value("${spring.application.name}")
    private String applicationName;


    @Autowired
    private HttpServletRequest request;

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(SystemUtil.getCpuCoreCount() * 2);

    @Autowired
    private void init(ApplicationContext applicationContext) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            threadPool.shutdownNow();
        }));
    }


    /**
     * 增加日志,仅适用于当前线程
     *
     * @param key
     * @param item
     */
    public void log(String key, Object item) {
        LogItems logItems = threadLocal.get();
        if (logItems == null) {
            logItems = startLog();
        }
        logItems.getLogs().add(LogItem.builder().key(key).item(item).createTime(System.currentTimeMillis()).build());
        log.info("{} -> {}", key, item);
    }


    /**
     * 获取日志项
     *
     * @return
     */
    public LogItems getLogItems() {
        return this.threadLocal.get();
    }

    /**
     * 开始记录日志
     */
    public LogItems startLog() {
        LogItems logItems = new LogItems();
        logItems.setCreateTime(System.currentTimeMillis());
        threadLocal.set(logItems);
        return logItems;
    }


    /**
     * 释放
     */
    public void endLog() {
        this.pushLogStream();
        threadLocal.remove();
    }


    /**
     * 推流
     */
    private void pushLogStream() {
        final LogItems logItems = this.threadLocal.get();
        if (logItems == null) {
            return;
        }
        //如果没有日志项
        if (logItems.isSendLog() == false && (logItems.getLogs() == null || logItems.getLogs().size() == 0)) {
            return;
        }


        final UserLogModel userLogModel = new UserLogModel();
        AuthDetails authDetails = this.authHelper.getCurrentUser();
        if (authDetails != null) {
            BeanUtils.copyProperties(authDetails, userLogModel);
        }

        Optional.ofNullable(logItems.getException()).ifPresent((exception) -> {
            userLogModel.setException(Map.of(
                    "message", exception.getMessage(),
                    "class", exception.getClass()
            ));
        });

        userLogModel.setUa(request.getHeader("User-Agent"));
        userLogModel.setIp(IPUtil.getRemoteIp(request));
        userLogModel.setUrl(request.getRequestURI());
        userLogModel.setCostTime(System.currentTimeMillis() - logItems.getCreateTime());
        userLogModel.setApplicationName(applicationName);
        userLogModel.setMethod(logItems.getMethod());
        userLogModel.setLogs(logItems.getLogs());
        userLogModel.setCreateTime(System.currentTimeMillis());

        threadPool.execute(() -> {
            try {
                this.streamHelper.send(
                        UserLogStreamName,
                        userLogModel
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LogItem {
        private String key;
        private Object item;
        private Long createTime;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LogItems {
        //异常
        private Exception exception;
        //是否发送
        boolean sendLog = false;
        //方法名
        private String method;
        //创建时间
        private Long createTime;
        //记录的日志
        private List<LogItem> logs = new ArrayList<>();
    }


}
