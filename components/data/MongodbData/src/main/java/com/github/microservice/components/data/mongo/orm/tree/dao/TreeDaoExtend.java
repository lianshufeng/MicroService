package com.github.microservice.components.data.mongo.orm.tree.dao;

import com.github.microservice.components.data.mongo.orm.tree.domain.TreeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public interface TreeDaoExtend<T extends TreeEntity> {


    T getParent(String id);


    /**
     * 创建一个节点，需要手动保存到数据库
     *
     * @param parentId
     * @return
     */
    T create(String parentId);

    /**
     * 创建一个节点，需要手动保存到数据库
     *
     * @return
     */
    T create();


    List<T> list(String parentId);

    Page<T> page(String parentId, Pageable pageable);


    /**
     * 查询所有的子节点
     */
    Criteria subPathCriteria(String[] path);


    Stream<T> recursiveLookup(AggregationOperation... aggregationOperation);


    /**
     *
     * 更新操作 + 递归统计
     *
     * @param treeEntity
     * @param updateAction
     */
//    void recursiveUpdate(TreeEntity treeEntity, UpdateAction updateAction);


    /**
     * 实现具体的递归算法
     *
     * @param parent
     * @param children
     * @return 返回父类需要更新的项
     */
//    Map<String, Object> recursiveAnalysis(T parent, Set<T> children);


    //递归更新容器,注：默认为hashmap， 如果担心oom，请替换为其他容器
//    Class<? extends Map> recursiveContainerClass();


//    @FunctionalInterface
//    public static interface UpdateAction {
//        void action();
//    }


}
