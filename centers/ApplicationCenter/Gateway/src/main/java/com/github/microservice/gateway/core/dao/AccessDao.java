package com.github.microservice.gateway.core.dao;

import com.github.microservice.gateway.core.domain.AccessRecord;
import com.github.microservice.gateway.core.model.Policy;
import com.github.microservice.core.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class AccessDao extends SuperRedisDao {

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 取出一个key下所有的数量
     *
     * @param key
     * @return
     */
    public long count(String key) {
        return redisTemplate.opsForList().size(key);
    }


    /**
     * 记录数据
     *
     * @param
     */
    public String record(Policy policy, AccessRecord accessRecord) {
        //设置系统时间
        accessRecord.setTime(System.currentTimeMillis());

        //到秒
        long time = Long.valueOf(System.currentTimeMillis() / 1000);

        //根据时间间隔存入
        time = Long.valueOf(time / policy.getCycleTime()) * policy.getCycleTime();

        //构建访问的key
        final String key = buildKey(accessRecord.getIp(), accessRecord.getRoleName(), String.valueOf(time));

        //入库
        this.redisTemplate.opsForList().rightPush(key, JsonUtil.toJson(accessRecord));

        //过期时间
        this.redisTemplate.expire(key, policy.getCycleTime(), TimeUnit.SECONDS);

//        this.redisTemplate.expire(key, 1, TimeUnit.DAYS);
        return key;
    }


    @Override
    public String tableName() {
        return "ac";
    }
}
