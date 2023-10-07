package demo.simple.controller;

import com.github.microservice.components.data.base.util.PageEntityUtil;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import demo.simple.dao.FixedMaterialStorageDao;
import demo.simple.dao.FixedMaterialStorageParentDao;
import demo.simple.dao.FixedMaterialStorageTrackRecordDao;
import demo.simple.domain.FixedMaterialStorage;
import demo.simple.domain.FixedMaterialStorageParent;
import demo.simple.domain.FixedMaterialStorageTrackRecord;
import demo.simple.model.FixedMaterialStorageModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@RestController
@RequestMapping("tree")
public class TreeController {

    @Autowired
    private FixedMaterialStorageDao fixedMaterialStorageDao;

    @Autowired
    private FixedMaterialStorageParentDao fixedMaterialStorageParentDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private FixedMaterialStorageTrackRecordDao fixedMaterialStorageTrackRecordDao;


    @RequestMapping("recursiveLookup2")
    public Object recursiveLookup2(String id) {
        final AggregationOperation[] aggregationOperations = new AggregationOperation[]{Aggregation.match(Criteria.where("_id").is(id))};

        FixedMaterialStorage fixedMaterialStorage = fixedMaterialStorageDao.recursiveLookup(aggregationOperations).reduce(new FixedMaterialStorage(), (it1, it2) -> {
            Map<String, Long> store = new HashMap<>();

            Optional.ofNullable((it1).getStore()).ifPresent(it -> {
                store.putAll(it);
            });

            it2.getStore().entrySet().forEach((entry) -> {
                store.put(entry.getKey(), entry.getValue() + store.getOrDefault(entry.getKey(), 0L));
            });
            return FixedMaterialStorage.builder().store(store).build();
        });

        return fixedMaterialStorage;
    }

    @RequestMapping("recursiveLookup")
    public Object recursiveLookup(String id) {
        final AggregationOperation[] aggregationOperations = new AggregationOperation[]{Aggregation.match(Criteria.where("_id").is(id))};

        FixedMaterialStorageParent fixedMaterialStorageParent = fixedMaterialStorageParentDao.recursiveLookup(aggregationOperations).reduce(new FixedMaterialStorageParent(), (it1, it2) -> {
            Map<String, Long> store = new HashMap<>();

            Optional.ofNullable((it1).getStore()).ifPresent(it -> {
                store.putAll(it);
            });

            it2.getStore().entrySet().forEach((entry) -> {
                store.put(entry.getKey(), entry.getValue() + store.getOrDefault(entry.getKey(), 0L));
            });
            return FixedMaterialStorageParent.builder().store(store).build();
        });

        return fixedMaterialStorageParent;
    }


    @RequestMapping("create")
    public Object create(String parentId) {
        FixedMaterialStorage fixedMaterialStorage = StringUtils.hasText(parentId) ? fixedMaterialStorageDao.create(parentId) : fixedMaterialStorageDao.create();
        this.mongoTemplate.save(fixedMaterialStorage);
        return toModel(fixedMaterialStorage);
    }


    @RequestMapping("list")
    public Object list(String parentId) {
        //这里使用多线程转换
        return fixedMaterialStorageDao.list(parentId).parallelStream().map(this::toModel);
    }

    @RequestMapping("page")
    public Object page(String parentId, Pageable pageable) {
        //这里使用多线程转换
        return PageEntityUtil.concurrent2PageModel(fixedMaterialStorageDao.page(parentId, pageable), this::toModel);
    }


    @RequestMapping("subPaths")
    public Object subPaths(String[] path, Pageable pageable) {
        Criteria criteria = this.fixedMaterialStorageDao.subPathCriteria(path);
        //这里使用多线程转换
        return PageEntityUtil.concurrent2PageModel(this.dbHelper.pages(Query.query(criteria), pageable, FixedMaterialStorage.class), this::toModel);
    }


//    @RequestMapping("recursiveUpdate")
//    public Object recursiveUpdate(String id) {
//        AtomicReference<FixedMaterialStorage> fixedMaterialStorageAtomicReference = new AtomicReference();
//        this.fixedMaterialStorageDao.findById(id).ifPresent((it) -> {
//            fixedMaterialStorageAtomicReference.set(it);
//            //递归更新
//            this.fixedMaterialStorageDao.recursiveUpdate(it, () -> {
//                it.setCurrentInventoryCount(it.getCurrentInventoryCount() + 1);
//                this.mongoTemplate.save(it);
//            });
//        });
//        return toModel(fixedMaterialStorageAtomicReference.get());
//    }


//    @RequestMapping("track")
//    public Object trackRecord(@RequestBody AddFixedMaterialStorageTrackRecord data) {
//        AtomicReference<FixedMaterialStorageTrackRecord> fixedMaterialStorageAtomicReference = new AtomicReference();
//        fixedMaterialStorageDao.findById(data.getStorageId()).ifPresent((storage) -> {
//            //
//            this.dbHelper.transaction((status) -> {
//
////                //增加流水
//                FixedMaterialStorageTrackRecord fixedMaterialStorageTrackRecord = new FixedMaterialStorageTrackRecord();
//                BeanUtils.copyProperties(data, fixedMaterialStorageTrackRecord);
//                fixedMaterialStorageTrackRecord.setEntity(storage);
//                this.dbHelper.saveTime(fixedMaterialStorageTrackRecord);
//                fixedMaterialStorageTrackRecordDao.save(fixedMaterialStorageTrackRecord);
//
//
//                //递归更新
//                this.fixedMaterialStorageDao.recursiveUpdate(storage, () -> {
//                    Update update = new org.springframework.data.mongodb.core.query.Update();
//                    update.inc("value", fixedMaterialStorageTrackRecord.getValue());
//                    this.dbHelper.updateTime(update);
//                    this.mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(storage.getId())), update, FixedMaterialStorage.class);
//                });
//
//                //ret
//                fixedMaterialStorageAtomicReference.set(fixedMaterialStorageTrackRecord);
//
//                return status;
//            });
//
//
//        });
//
//        if (fixedMaterialStorageAtomicReference.get() == null) {
//            return null;
//        }
//
//        Map<String, Object> ret = new HashMap<>(BeanMap.create(fixedMaterialStorageAtomicReference.get()));
//        ret.remove("entity");
//        return ret;
//    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddFixedMaterialStorageTrackRecord extends FixedMaterialStorageTrackRecord {
        private String storageId;
    }


    public FixedMaterialStorageModel toModel(FixedMaterialStorage fixedMaterialStorage) {
        FixedMaterialStorageModel fixedMaterialStorageModel = new FixedMaterialStorageModel();
        BeanUtils.copyProperties(fixedMaterialStorage, fixedMaterialStorageModel, "parent");
        Optional.ofNullable(fixedMaterialStorage.getParent()).ifPresent((it) -> {
            fixedMaterialStorageModel.setParent(it.getId());
        });
        return fixedMaterialStorageModel;
    }


}
