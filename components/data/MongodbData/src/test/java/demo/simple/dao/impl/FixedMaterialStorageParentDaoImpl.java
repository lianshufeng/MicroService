package demo.simple.dao.impl;

import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.github.microservice.components.data.mongo.orm.tree.dao.impl.TreeDaoImpl;
import com.github.microservice.components.data.mongo.orm.tree.domain.TreeEntity;
import demo.simple.dao.extend.FixedMaterialStorageParentDaoExtend;
import demo.simple.domain.FixedMaterialStorage;
import demo.simple.domain.FixedMaterialStorageParent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class FixedMaterialStorageParentDaoImpl extends TreeDaoImpl<FixedMaterialStorageParent> implements FixedMaterialStorageParentDaoExtend {


    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;


//    @Autowired
    private void initData(ApplicationContext applicationContext) {

        FixedMaterialStorage latestFixedMaterialStorage = null;
        for (int i = 0; i < 10; i++) {
            FixedMaterialStorage fixedMaterialStorage = new FixedMaterialStorage();
            fixedMaterialStorage.setName("child" + i);
            String c = String.valueOf((char) (65 + new Random().nextInt(26)));
            fixedMaterialStorage.setStore(Map.of("index", Integer.valueOf(i).longValue(), c, Integer.valueOf(new Random().nextInt(30)).longValue()));
            fixedMaterialStorage.setParent(latestFixedMaterialStorage);
            dbHelper.saveTime(fixedMaterialStorage);
            this.mongoTemplate.save(fixedMaterialStorage);
            latestFixedMaterialStorage = fixedMaterialStorage;
        }



        FixedMaterialStorageParent fixedMaterialStorageParent2 = new FixedMaterialStorageParent();
        fixedMaterialStorageParent2.setName("parent2");
        fixedMaterialStorageParent2.setStore(Map.of("p", Integer.valueOf(new Random().nextInt(30)).longValue()));
        this.mongoTemplate.save(fixedMaterialStorageParent2);



        FixedMaterialStorageParent fixedMaterialStorageParent = new FixedMaterialStorageParent();
        fixedMaterialStorageParent.setName("parent");
        fixedMaterialStorageParent.setStore(Map.of("p", Integer.valueOf(new Random().nextInt(30)).longValue()));
        fixedMaterialStorageParent.setReference(latestFixedMaterialStorage);
        fixedMaterialStorageParent.setParent(fixedMaterialStorageParent2);
        this.mongoTemplate.save(fixedMaterialStorageParent);


    }


}
