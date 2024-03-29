package com.github.microservice.components.data.mongo.mongo.helper;

import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import com.github.microservice.core.util.bean.BeanUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 重置索引
 */
@Slf4j
@Component
public class ReIndexHelper {

    @Autowired
    private MongoTemplate mongoTemplate;

    //每个索引里最大数量
    private final static int MaxIndexCount = 24;


    /**
     * 重建索引
     */
    public void reIndexFromField(Class<? extends SuperEntity> entityClass, String fieldName, Class<?> cls) {
        //构建索引
        Set<Index> nowIndexNames = BeanUtil.readBeanType(cls).keySet()
                .stream()
                .map((it) -> {
                    final String indexName = fieldName + "." + it;
                    return new Index().named(indexName).on(indexName, Sort.Direction.ASC);
                }).collect(Collectors.toSet());

        //更新索引
        updateIndex(entityClass, fieldName, nowIndexNames.toArray(new Index[0]));
    }


    /**
     * 重置索引
     */
    public void reIndexFromMap(Class<? extends SuperEntity> entityClass, String fieldName) {
        //构建索引
        Set<Index> nowIndexNames = getIndexNamesFromMap(entityClass, fieldName)
                .stream()
                .map((it) -> {
                    final String indexName = fieldName + "." + it;
                    return new Index().named(indexName).on(indexName, Sort.Direction.ASC);
                }).collect(Collectors.toSet());

        //更新索引
        updateIndex(entityClass, fieldName, nowIndexNames.toArray(new Index[0]));
    }


    /**
     * 取出现有点索引
     *
     * @return
     */
    public Set<String> getIndexNames(String tableName) {
        //现有索引
        return this.mongoTemplate.indexOps(tableName)
                .getIndexInfo()
                .stream()
                .map((it) -> {
                    return it.getName();
                }).collect(Collectors.toSet());
    }


    public void copyIndex(String tableName, String newTableName) {
        final IndexOperations indexOperations = this.mongoTemplate.indexOps(tableName);
        indexOperations.getIndexInfo().stream().filter(it -> !Set.of("_id_", "_id").contains(it.getName())).forEach((indexInfo) -> {
            final String indexName = indexInfo.getName();
            final IndexOperations newIndexOperations = this.mongoTemplate.indexOps(newTableName);

            //删除索引
            newIndexOperations.getIndexInfo().stream().filter(it -> indexName.equals(it.getName())).forEach((it) -> {
                newIndexOperations.dropIndex(it.getName());
            });
            final Index newIndex = new Index();
            newIndex.named(indexName);
            indexInfo.getIndexFields().forEach((it) -> {
                newIndex.on(it.getKey(), it.getDirection());
            });

            //创建索引
            newIndexOperations.ensureIndex(newIndex);
        });
    }


    /**
     * 取出现有点索引
     *
     * @param entityClass
     * @return
     */
    public Set<String> getIndexNames(Class<? extends SuperEntity> entityClass) {
        return this.getIndexNames(this.mongoTemplate.getCollectionName(entityClass));
    }


    /**
     * 更新索引
     *
     * @param tableName
     * @param indexs
     */
    @SneakyThrows
    public void updateIndex(String tableName, Index... indexs) {
        //现有索引
        final Set<String> nowIndexNames = getIndexNames(tableName);

        //索引
        final IndexOperations indexOperations = this.mongoTemplate.indexOps(tableName);

        //索引不存在建索引
        Arrays.stream(indexs).filter(it -> it.getIndexKeys().size() > 0).forEach((it) -> {
            String indexName = getIndexName(it);
            if ((nowIndexNames.contains(indexName))) {
                indexOperations.dropIndex(indexName);
            }
            String ret = indexOperations.ensureIndex(it);
            log.info("update index : " + ret);
        });
    }


    @SneakyThrows
    private String getIndexName(Index index) {
        final Field field = FieldUtils.getDeclaredField(Index.class, "name", true);
        if (field == null) {
            return null;
        }
        Object val = field.get(index);
        if (val == null) {
            return null;
        }
        return String.valueOf(val);
    }

    /**
     * 更新索引，独立的索引项
     *
     * @param entityClass
     * @param indexs
     */
    @SneakyThrows
    public void updateIndex(Class<? extends SuperEntity> entityClass, Index... indexs) {
        this.updateIndex(this.mongoTemplate.getCollectionName(entityClass), indexs);
    }


    /**
     * 更新索引,共用一个索引项
     *
     * @param entityClass
     */
    @SneakyThrows
    public void updateIndex(Class<? extends SuperEntity> entityClass, String indexName, Index... indexs) {
        boolean isUpdate = false;
        List<IndexInfo> indexInfos = this.mongoTemplate.indexOps(entityClass)
                .getIndexInfo()
                .stream()
                .filter((it) -> {
                    return it.getName().length() > indexName.length() + 1 && it.getName().substring(0, indexName.length()).equals(indexName);
                }).collect(Collectors.toList());
        if (indexInfos != null && indexInfos.size() > 0) {
            isUpdate = isNeedUpdateIndex(indexInfos, indexs);
        } else {
            isUpdate = true;
        }

        //进行更新索引
        if (isUpdate) {
            final IndexOperations indexOperations = this.mongoTemplate.indexOps(entityClass);

            //如果存在则删除所有的和符合索引
            indexOperations.getIndexInfo().stream().filter((it) -> {
                return it.getName().length() > indexName.length() + 1 && it.getName().substring(0, indexName.length()).equals(indexName);
            }).forEach((it) -> {
                indexOperations.dropIndex(it.getName());
            });

            int size = (int) (indexs.length / MaxIndexCount);
            for (int i = 0; i < size; i++) {
                updateIndex(indexOperations, indexName + "_" + i, i, indexs);
            }
            if (indexs.length % MaxIndexCount != 0) {
                updateIndex(indexOperations, indexName + "_" + size, size, indexs);
            }
        }
    }

    @SneakyThrows
    private void updateIndex(IndexOperations indexOperations, String indexName, int page, final Index[] indexs) {
        Index index = new Index();
        index.named(indexName);

        for (int j = 0; j < MaxIndexCount; j++) {
            //索引
            int i = page * MaxIndexCount + j;
            if (i >= indexs.length) {
                continue;
            }
            Index it = indexs[i];
            Field field = it.getClass().getDeclaredField("name");
            field.setAccessible(true);
            String fieldName = String.valueOf(field.get(it));
            index.on(fieldName, Sort.Direction.ASC);
        }
        log.info("update index : {}", indexName);
        indexOperations.ensureIndex(index);

    }


    /**
     * 需要更新的名字
     *
     * @return
     */
    private boolean isNeedUpdateIndex(List<IndexInfo> indexInfos, Index[] indexs) {
        //取出需要更新的索引名
        Set<String> newUpdateNames = Arrays.stream(indexs).map((it) -> {
            return it.getIndexOptions().get("name");
        }).filter((it) -> {
            return it != null;
        }).map((it) -> {
            return String.valueOf(it);
        }).collect(Collectors.toSet());

        //取出现有的索引名
        Set<String> nowIndexName = new HashSet<>();
        indexInfos.forEach((it) -> {
            nowIndexName.addAll(it.getIndexFields().stream().map((index) -> {
                return index.getKey();
            }).collect(Collectors.toSet()));
        });


        for (String updateName : newUpdateNames) {
            if (!nowIndexName.contains(updateName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 取出Map属性对应的所有key的集合
     *
     * @return
     */
    public Set<String> getIndexNamesFromMap(Class<? extends SuperEntity> entityClass, String fieldName) {
        String collectionName = this.mongoTemplate.getCollectionName(entityClass);
        String map = "function(){if(this." + fieldName + "==null){return}for(var key in this." + fieldName + "){emit(key,1)}};";
        String reduce = "function(key,values){return values.length};";
        Set<String> indexNames = new HashSet<>();
        MapReduceResults<Map> mapReduceResults = this.mongoTemplate.mapReduce(collectionName, map, reduce, Map.class);
        mapReduceResults.forEach((it) -> {
            indexNames.add(String.valueOf(it.get("_id")));
        });
        return indexNames;
    }


}
