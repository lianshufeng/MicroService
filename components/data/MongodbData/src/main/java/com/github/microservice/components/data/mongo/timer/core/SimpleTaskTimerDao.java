package com.github.microservice.components.data.mongo.timer.core;

import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.github.microservice.components.data.mongo.mongo.helper.MongoQueryLanguageHelper;
import com.github.microservice.components.data.mongo.mongo.model.QueryModel;
import com.github.microservice.components.data.mongo.timer.conf.TaskTimerConf;
import com.github.microservice.components.data.mongo.timer.domain.SimpleTaskTimerTable;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 任务调度器的实现
 */

@Component
@Scope("prototype")
public class SimpleTaskTimerDao {

    @Setter
    @Getter
    private Class<? extends SuperEntity> taskTimerTableCls;

    @Autowired
    private TaskTimerConf taskTimerConf;


    @Autowired
    private MongoTemplate mongoTemplate;


    @Autowired
    private MongoQueryLanguageHelper mongoQueryLanguageHelper;

    @Autowired
    private DBHelper dbHelper;


    /**
     * 查询所有的任务
     *
     * @return
     */
    public List<SimpleTaskTimerTable> list() {
        //查询条件
        QueryModel queryModel = new QueryModel().setMql("{}").setFields(new HashSet<>() {{
            add("_id");
            add("cron");
            add("disable");
        }});
        //分页条件
        Pageable pageable = PageRequest.of(0, this.taskTimerConf.getMaxLoadDBCount(), Sort.by(Sort.Direction.ASC, "updateTime"));
        Page<Document> entities = this.mongoQueryLanguageHelper.queryByMql(queryModel, pageable, this.mongoTemplate.getCollectionName(taskTimerTableCls));
        return entities.getContent().parallelStream().map((it) -> {
            SimpleTaskTimerTable simpleTaskTimerTable = new SimpleTaskTimerTable();
            simpleTaskTimerTable.setId(it.getObjectId("_id").toString());
            simpleTaskTimerTable.setCron(it.getString("cron"));
            Optional.ofNullable(it.getBoolean("disable")).ifPresent((disable) -> {
                simpleTaskTimerTable.setDisable(disable);
            });
            return simpleTaskTimerTable;
        }).collect(Collectors.toList());
    }


    /**
     * 通过id找到这条数据
     *
     * @param id
     * @return
     */
    public <T> T findById(String id) {
        return (T) this.mongoTemplate.findById(id, this.taskTimerTableCls);
    }

    /**
     * 查询并转换为任务调度器对象
     *
     * @param id
     * @return
     */
    public SimpleTaskTimerTable findByIdToSimpleTaskTimerTable(String id) {
        Map map = this.mongoTemplate.findById(id, Map.class, this.mongoTemplate.getCollectionName(this.taskTimerTableCls));
        if (map == null) {
            return null;
        }
        SimpleTaskTimerTable simpleTaskTimerTable = new SimpleTaskTimerTable();
        simpleTaskTimerTable.setId(id);
        Optional.ofNullable(map.get("cron")).ifPresent((it) -> {
            simpleTaskTimerTable.setCron(String.valueOf(it));
        });
        Optional.ofNullable(map.get("disable")).ifPresent((it) -> {
            simpleTaskTimerTable.setDisable((boolean) it);
        });
        return simpleTaskTimerTable;
    }


    /**
     * 执行锁定时间
     */
    public boolean lockTime(String id) {
        final String lockSession = UUID.randomUUID().toString();
        Query query = Query.query(
                Criteria.where("_id").is(id).and("executeLockTIme").lt(this.dbHelper.getTime())
        );
        Update update = new Update();
        update.set("executeLockTIme", (this.dbHelper.getTime() + this.taskTimerConf.getExecuteLockTIme()));
        update.set("lockSession", lockSession);
        this.dbHelper.updateTime(update);

        //取出数据实体
        final SimpleTaskTimerTable simpleTaskTimerTable = (SimpleTaskTimerTable) this.mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), getTaskTimerTableCls());
        return simpleTaskTimerTable != null && lockSession.equals(simpleTaskTimerTable.getLockSession());
    }

    /**
     * 解除锁定时间
     *
     * @param id
     */
    public void unlockTime(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update();
        update.set("executeLockTIme", 0);
        this.dbHelper.updateTime(update);
        this.mongoTemplate.updateFirst(query, update, getTaskTimerTableCls());
    }


}
