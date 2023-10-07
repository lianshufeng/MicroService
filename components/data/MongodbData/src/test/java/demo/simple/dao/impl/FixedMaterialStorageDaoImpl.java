package demo.simple.dao.impl;

import com.github.microservice.components.data.mongo.orm.tree.dao.impl.TreeDaoImpl;
import com.github.microservice.components.data.mongo.orm.tree.domain.TreeEntity;
import demo.simple.dao.extend.FixedMaterialStorageDaoExtend;
import demo.simple.domain.FixedMaterialStorage;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class FixedMaterialStorageDaoImpl extends TreeDaoImpl<FixedMaterialStorage> implements FixedMaterialStorageDaoExtend {


//    @Override
//    public Map<String, Object> recursiveAnalysis(FixedMaterialStorage parent, Set<FixedMaterialStorage> children) {
//        System.out.println(children);
//        AtomicLong pendingOutboundCount = new AtomicLong(0);
//        AtomicLong pendingInboundCount = new AtomicLong(0);
//        AtomicLong currentInventoryCount = new AtomicLong(0);
//        AtomicReference<BigDecimal> value = new AtomicReference<>(new BigDecimal(0));
//
//
//        children.forEach((it) -> {
//            pendingOutboundCount.addAndGet(it.getPendingOutboundCount());
//            pendingInboundCount.addAndGet(it.getPendingInboundCount());
//            currentInventoryCount.addAndGet(it.getCurrentInventoryCount());
//            Optional.ofNullable(it.getValue()).ifPresent((val) -> {
//                value.set(value.get().add(val));
//            });
//        });
//
//        return Map.of(
//                "pendingOutboundCount", pendingOutboundCount.get(),
//                "pendingInboundCount", pendingInboundCount.get(),
//                "currentInventoryCount", currentInventoryCount.get(),
//                "childrenCount", children.size(),
//                "value", value.get()
//
//        );
//    }



}

