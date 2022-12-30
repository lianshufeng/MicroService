package com.github.microservice.components.data.mongo.timer.domain;


import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * 任务定时器
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
public class SimpleTaskTimerTable extends SuperEntity {

    //cron表达式，如: 0/10 * * * * * , 每10秒执行一次
    private String cron;


    //是否允许
    @Indexed
    private boolean disable;


    //执行锁定时间
    @Indexed
    private long executeLockTIme;

    //锁定的会话id
    @Indexed
    private String lockSession;

}
