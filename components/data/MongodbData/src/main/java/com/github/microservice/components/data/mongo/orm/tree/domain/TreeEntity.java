package com.github.microservice.components.data.mongo.orm.tree.domain;

import com.github.microservice.components.data.mongo.mongo.domain.SuperEntity;
import com.github.microservice.core.util.bean.BeanUtil;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
public abstract class TreeEntity extends SuperEntity {

    //父路径
    @Indexed
    @DBRef(lazy = true)
    private TreeEntity parent;

    // String.join("/", paths), paths 为字符串数组，依次为路径的节点id
    @Indexed
    private String path;

    //排序字段
    @Indexed
    private int order;

    //引用
    @Indexed
    @DBRef(lazy = true)
    private TreeEntity reference;


    public static <T extends TreeEntity> T build(Class<? extends TreeEntity> cls, String id) {
        T t = (T) BeanUtil.newClass(cls);
        t.setId(id);
        return t;
    }


//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class Reference {
//
//        @DBRef(lazy = true)
//        private TreeEntity imp;
//
//        @DBRef(lazy = true)
//        private TreeEntity exp;
//
//
//        public static Reference buildImport(TreeEntity imp) {
//            Reference ref = new Reference();
//            ref.setImp(imp);
//            return ref;
//        }
//
//
//        public static Reference buildExport(TreeEntity exp) {
//            Reference ref = new Reference();
//            ref.setExp(exp);
//            return ref;
//        }
//
//
//    }

}
