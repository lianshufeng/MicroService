package com.github.microservice.components.data.mongo.timer.conf;

import com.github.microservice.core.util.os.SystemUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ConfigurationProperties(prefix = "task.timer")
public class TaskTimerConf {

    //最大载入数据库数量
    private int maxLoadDBCount = 100000;

    //通知事件的最大线数量
    private int maxThreadPoolCount = SystemUtil.getCpuCoreCount() * 2;

}
