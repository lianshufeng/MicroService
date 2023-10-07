package demo.simple.dao;

import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;
import demo.simple.dao.extend.FixedMaterialStorageDaoExtend;
import demo.simple.domain.FixedMaterialStorage;

public interface FixedMaterialStorageDao extends MongoDao<FixedMaterialStorage>, FixedMaterialStorageDaoExtend {
}
