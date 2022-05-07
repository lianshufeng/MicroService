package com.github.microservice.centers.experiment.query.core.dao.impl;

import com.github.microservice.centers.experiment.query.core.dao.extend.ExperimentDaoExtend;
import com.github.microservice.centers.experiment.query.core.domain.Experiment;
import com.github.microservice.centers.experiment.query.core.domain.UserGroup;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ExperimentDaoImpl implements ExperimentDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public List<Experiment> queryByUidAndServiceName(String uid, String serviceName) {


//        Aggregation customerAggregation = Aggregation.newAggregation(
        // 过滤用户
//                Aggregation.match(Criteria.where("uid").is(uid)),
        //关联dbref查询
//                Aggregation.lookup("experiment", "experiment.$id", "_id", "experiment_lookup")
//                数组分割成每条记录
//                Aggregation.unwind("experiment"),
        //更换为根目录
//                Aggregation.replaceRoot("$experiment"),
        //判定映射关系是否存在 "mapping.service1": { $exists : true }
//                Aggregation.match(Criteria.where(String.format("mapping.%s", serviceName)).exists(true))
        //过滤重复
//                Aggregation.group("_id").first("$_id").as("_oid").first("name").as("name").first("mapping").as("mapping"),
        //过滤_id，提取需要显示的字段
//                Aggregation.project("name", "mapping")

//        );
        //磁盘
//                .withOptions(AggregationOptions.builder().allowDiskUse(true).build());

        //使用mongo驱动, Aggregation.newAggregation 在dbref支持有问题
        List<? extends Bson> pipeline = Arrays.asList(
                // 过滤用户
                new Document().append("$match", new Document()
                        .append("uid", uid)
                ),
                //关联dbref查询
                new Document().append("$lookup", new Document()
                        .append("from", "experiment")
                        .append("localField", "experiment.$id")
                        .append("foreignField", "_id")
                        .append("as", "experiment")
                ),
                // 数组分割成每条记录
                new Document().append("$unwind", "$experiment"),
                //更换为根目录
                new Document().append("$replaceRoot", new Document()
                        .append("newRoot", "$experiment")
                ),
//                判定映射关系是否存在 "mapping.service1": { $exists : true }
                new Document().append("$match", new Document()
                        .append(String.format("mapping.%s", serviceName), new Document()
                                .append("$exists", true)
                        )
                ),
                //过滤重复
                new Document().append("$group", new Document()
                        .append("_id", "$_id")
                        .append("name", new Document()
                                .append("$first", "$name")
                        )
                        .append("mapping", new Document()
                                .append("$first", "$mapping")
                        )
                        .append("order", new Document()
                                .append("$first", "$order")
                        )
                ),
                //过滤_id，提取需要显示的字段
                new Document().append("$project", new Document()
                        .append("name", 1)
                        .append("mapping", 1)
                        .append("order", 1)
                ),
                //最后结果排序,升序
                new Document()
                        .append("$sort", new Document()
                                .append("order", 1)
                        )
        );

        final MongoCollection<Document> collection = this.mongoTemplate.getCollection(this.mongoTemplate.getCollectionName(UserGroup.class));
        final Spliterator<Experiment> experimentSpliterator = collection.aggregate(pipeline)
                .allowDiskUse(false)
                .map(it -> {
                    Experiment experiment = new Experiment();
                    experiment.setName(it.getString("name"));
                    experiment.setMapping((Map<String, String>) it.get("mapping"));
                    experiment.setOrder(it.getInteger("order"));
                    return experiment;
                })
                .spliterator();
        return StreamSupport.stream(experimentSpliterator, false).collect(Collectors.toList());
    }
}
