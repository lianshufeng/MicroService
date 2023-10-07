package com.github.microservice.components.data.mongo.orm.tree.dao.impl;

import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.github.microservice.components.data.mongo.orm.tree.dao.TreeDaoExtend;
import com.github.microservice.components.data.mongo.orm.tree.domain.TreeEntity;
import com.github.microservice.components.data.mongo.token.service.ResourceTokenService;
import com.github.microservice.core.util.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.bson.BsonRegularExpression;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
public abstract class TreeDaoImpl<T extends TreeEntity> implements TreeDaoExtend<T> {

    private static final String ResTokenNameTemplate = "%s_%s";
    private static final String BeanMapUpdateNames = "updateNames";
    private static final String BeanMapBean = "bean";


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private ResourceTokenService resourceTokenService;

    @Autowired
    private MappingMongoConverter mappingMongoConverter;


    //缓存
    private Class<? extends TreeEntity> entityClass;

    @Autowired
    private void init(ApplicationContext applicationContext) {
        this.entityClass = entityClass();
    }


//    @Override
//    public Class<? extends Map> recursiveContainerClass() {
//        return ConcurrentHashMap.class;
//    }


    @Override
    public Criteria subPathCriteria(String[] path) {
        return Criteria.where("path").regex(new BsonRegularExpression("^" + String.join("/", path) + "/" + "*", "m"));
    }

    @Override
    public Page<T> page(String parentId, Pageable pageable) {
        Query query = Query.query(Criteria.where("parent").is(TreeEntity.build(entityClass, parentId)));
        query.with(Sort.by(Sort.Direction.ASC, "order"));
        return (Page<T>) this.dbHelper.pages(query, pageable, entityClass);
    }

    @Override
    public List<T> list(String parentId) {
        Query query = Query.query(Criteria.where("parent").is(TreeEntity.build(entityClass, parentId)));
        query.with(Sort.by(Sort.Direction.ASC, "order"));
        return this.mongoTemplate.find(query, entityClass());
    }


    @Override
    public T getParent(String id) {
        return (T) mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), entityClass());
    }

    @Override
    public T create() {
        T current = (T) BeanUtil.newClass(entityClass);
        dbHelper.saveTime(current);
        return current;
    }

    @Override
    public T create(final String parentId) {
        final T parent = getParent(parentId);
        T current = create();
        current.setParent(parent);
        current.setPath(appendPath(parent.getPath(), parentId));
        dbHelper.saveTime(current);
        return current;
    }


//    @Override
//    public void recursiveUpdate(final TreeEntity entity, final UpdateAction updateMethod) {
//        //为了提高效率，使用并行流
//        AtomicReference<String> rootId = new AtomicReference<>();
//        //如果没有父类，那么就是根节点
//        if (entity.getParent() == null) {
//            rootId.set(entity.getId());
//        } else {
//            //取出根节点
//            Optional.ofNullable(entity.getPath()).ifPresent((path) -> {
//                String[] paths = path.split("/");
//                rootId.set(paths[0]);
//            });
//        }
//        //锁住根节点 , collectionName+id
//        final String resourceName = String.format(ResTokenNameTemplate, this.mongoTemplate.getCollectionName(entityClass), rootId.get());
//        //锁住资源
//        @Cleanup ResourceTokenService.Token token = this.resourceTokenService.token(resourceName);
//        try {
//            //执行更新
//            updateMethod.action();
//            //递归统计并更新
//            _recursiveUpdate(rootId.get());
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error(e.getMessage());
//        }
//
//
//    }


    /**
     * 递归处理
     */
//    private void _recursiveUpdate(String rootId) {
//        final Map<String, BeanMapContainer> recursiveContainer = recursiveContainer(rootId);
//
//        long startTime = System.currentTimeMillis();
//
//        // 取出所有路径中的所有节点id
//        final Set<String> pathParentIds = new HashSet<>();
//        recursiveContainer.values().stream().map(it -> it.getParentId()).filter(it -> it != null).forEach(it -> {
//            pathParentIds.add(it);
//        });
//
//        //找到所有的叶子结点： 叶子结点不能作为父节点
//        final Set<BeanMapContainer> leafNodes = recursiveContainer.values().stream().filter(it -> !pathParentIds.contains(it.getId())).collect(Collectors.toSet());
//        final Set<String> leafNodesIds = leafNodes.stream().map(it -> it.getId()).collect(Collectors.toSet());
//
//
//        // 找到这些叶子结点的父节点, 且该父节点的所有子节点均为叶子结点（从最低等父节点开始遍历）
//        final Set<String> leafNodesParentIds = new HashSet<>();
//        leafNodes.stream().map(it -> it.getParentId()).forEach(it -> leafNodesParentIds.add(it));
//        final Set<BeanMapContainer> latestParents = leafNodesParentIds.stream().map(it -> recursiveContainer.get(it)).filter((parent) -> {
//            AtomicBoolean isLatestParent = new AtomicBoolean(true);
//            parent.getChildren().stream().filter(it -> !leafNodesIds.contains(it)) //过滤父节点的所有子节点均为叶子结点
//                    .findFirst().ifPresent(it -> {
//                        isLatestParent.set(false);
//                    });
//            return isLatestParent.get();
//        }).collect(Collectors.toSet());
//
//        // 开始递归统计
//        executeRecursiveUpdate(recursiveContainer, latestParents);
//
//        System.out.println("time : " + (System.currentTimeMillis() - startTime));
//
//
//        //同步到数据库里
//        this.containerToDataBase(recursiveContainer);
//
//    }


    // 检查会变更的字段并批量同步到数据库里
//    private void containerToDataBase(final Map<String, BeanMapContainer> recursiveContainer) {
//        final BulkOperations bulkOperations = this.mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, entityClass);
//        recursiveContainer.values().parallelStream().filter(it -> it.getUpdateNames().size() > 0).forEach((node) -> {
//            final Update update = new Update();
//            node.getUpdateNames().forEach((name) -> {
//                var value = node.getBean().get(name);
//                update.set(name, value);
//            });
//            this.dbHelper.updateTime(update);
//            bulkOperations.updateOne(Query.query(Criteria.where("_id").is(node.getId())), update);
//        });
//        bulkOperations.execute();
//    }


//    private void executeRecursiveUpdate(final Map<String, BeanMapContainer> recursiveContainer, final Set<BeanMapContainer> parentNodes) {
//
//        parentNodes.stream().forEach(parentNode -> {
//            // 子节点
//            final List<BeanMapContainer> childrenNodes = parentNode.getChildren().stream().map(it -> recursiveContainer.get(it)).collect(Collectors.toList());
//            // 子节点转为数据库实体
//            final Set<? extends TreeEntity> childrenNodesEntity = childrenNodes.parallelStream().map(it -> {
//                return mappingMongoConverter.read(entityClass, new Document(it.getBean()));
//            }).collect(Collectors.toSet());
//
//            //分析父节点更新
//            final Map<String, Object> parentNodeUpdate = recursiveAnalysis(null, (Set<T>) childrenNodesEntity);
//            if (parentNodeUpdate != null && parentNodeUpdate.size() > 0) {
//
//                parentNodeUpdate.entrySet().forEach(entry -> {
//                    final var oldValue = parentNode.getBean().get(entry.getKey());
//                    boolean isUpdate = false;
//                    if (entry.getValue() == null && oldValue != null) {
//                        isUpdate = true;
//                    } else if (entry.getValue() != null && oldValue != null && !entry.getValue().equals(oldValue)) {
//                        isUpdate = true;
//                    }
//
//                    if (isUpdate) {
//                        parentNode.getUpdateNames().add(entry.getKey());
//                        parentNode.getBean().put(entry.getKey(), entry.getValue());
//                    }
//                });
//
//
//                parentNode.getUpdateNames().addAll(parentNodeUpdate.keySet());
//                parentNode.getBean().putAll(parentNodeUpdate);
//            }
//        });
//
//
//        Set<BeanMapContainer> superParentNodes = parentNodes.stream().filter(it -> it.getParentId() != null).map(it -> recursiveContainer.get(it.getParentId())).collect(Collectors.toSet());
//        if (superParentNodes != null && superParentNodes.size() > 0) {
//            executeRecursiveUpdate(recursiveContainer, superParentNodes);
//        }
//    }


    /**
     * 构建树的资源容器
     *
     * @return
     */
//    private Map<String, BeanMapContainer> recursiveContainer(String rootId) {
//        //实例化容器
//        final Map<String, BeanMapContainer> recursiveContainer = BeanUtil.newClass(recursiveContainerClass());
//
//        //数据库中查询所有的兄弟节点并装入到递归容器里
//        Set<String> projectNames = Arrays.stream(BeanUtils.getPropertyDescriptors(entityClass)).map(it -> it.getName())//转换为字段名
//                .filter(it -> !Set.of("parent", "class", "new").contains(it))//过滤特殊字段名和父类，避免懒加载
//                .collect(Collectors.toSet());
//        projectNames.add("parentId");
//        Aggregation customerAgg = Aggregation.newAggregation(Aggregation.match(new Criteria().orOperator(subPathCriteria(new String[]{rootId}), Criteria.where("_id").is(rootId))), Aggregation.addFields().addField("_tmp" + System.currentTimeMillis()).withValue(0).build(),// fix bug
//                Aggregation.addFields().addFieldWithValue("parentId", new Document("$toString", "$parent.$id")).build(),// add parentId
//                Aggregation.project(projectNames.toArray(String[]::new))).withOptions(
//                //磁盘
//                AggregationOptions.builder().allowDiskUse(true).build());
//        this.mongoTemplate.aggregate(customerAgg, entityClass, Map.class).getMappedResults().stream().forEach((it) -> {
//            final Map<String, Object> bean = new HashMap<>() {{
//                putAll(it);
//            }};
//            final String id = String.valueOf(bean.get("_id"));
//            bean.remove("_id");
//            bean.put("id", id);
//            final String parentId = bean.get("parentId") == null ? null : String.valueOf(it.get("parentId"));
//            final String[] path = bean.get("path") == null ? null : String.valueOf(it.get("path")).split("/");
//
//
//            BeanMapContainer beanMapContainer = new BeanMapContainer();
//            beanMapContainer.setId(id);
//            beanMapContainer.setParentId(parentId);
//            beanMapContainer.setBean(bean);
//            beanMapContainer.setPath(path);
//
//            // 对象转换成map
//            recursiveContainer.put(id, beanMapContainer);
//        });
//
//
//        // children
//        recursiveContainer.values().stream().forEach((node) -> {
//            final String[] parentPath = ArrayUtils.add(node.getPath() == null ? new String[]{} : node.getPath(), node.getId());
//            // children nodes sort ?
//            node.getChildren().addAll(recursiveContainer.values().stream().filter(it -> Arrays.equals(it.getPath(), parentPath)).map(it -> it.getId()).collect(Collectors.toSet()));
//        });
//
//        return recursiveContainer;
//    }


    /**
     * 在路径里增加一个父类的id
     *
     * @param paths
     * @param parentId
     * @return
     */
    protected String appendPath(String paths, String parentId) {
        final List<String> items = paths == null ? new ArrayList<>() : new ArrayList<>() {{
            addAll(Arrays.asList(paths.split("/")));
        }};
        items.add(parentId);
        return String.join("/", items.toArray(String[]::new));
    }


    @Override
    public Stream<T> recursiveLookup(AggregationOperation... aggregationOperation) {
        final String collectionName = this.mongoTemplate.getCollectionName(entityClass);

        //功能:递归查询,分割数组,替换根
        final AggregationOperation[] coreAggregationOperations = new AggregationOperation[]{Aggregation.graphLookup(collectionName).startWith("$_id").connectFrom("_id").connectTo("parent.$id").as("_children"),//graphLookup
                Aggregation.unwind("_children"), //unwind
                Aggregation.replaceRoot("_children")//replaceRoot
        };

        Aggregation aggregations = Aggregation.newAggregation(
                ArrayUtils.addAll(aggregationOperation, coreAggregationOperations) //aggregations
        ).withOptions(AggregationOptions.builder().allowDiskUse(false).build());


        return this.mongoTemplate.aggregate(aggregations, this.entityClass, this.entityClass).getMappedResults().stream()//toStream
                .map(this::recursiveReference);// 处理递归引用
    }


    //todo ， 不允许递归统计chilren
    //todo ， 不允许递归统计引用

    private T recursiveReference(TreeEntity it) {
        Optional.ofNullable(it.getReference()).ifPresent((ref) -> {
            //dbref再次cglib
            Optional.ofNullable(BeanMap.create(ref).get("target")).ifPresent((target) -> {
                BeanUtils.copyProperties(target, it, "_id", "parent","reference");
            });
        });
        return (T) it;
    }


    private Class entityClass() {
        return (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

//    @Data
//    private class BeanMapContainer {
//
//        @Getter
//        @Setter
//        private String id;
//
//        @Getter
//        @Setter
//        private String parentId;
//
//        @Getter
//        private Set<String> children = new HashSet<>();
//
//        @Getter
//        @Setter
//        private String[] path;
//
//        @Setter
//        @Getter
//        private Map<String, Object> bean;
//
//
//        @Getter
//        private Set<String> updateNames = new HashSet<>();
//
//
//    }
}
