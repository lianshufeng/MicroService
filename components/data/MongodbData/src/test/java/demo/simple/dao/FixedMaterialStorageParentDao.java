package demo.simple.dao;

import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;
import demo.simple.dao.extend.FixedMaterialStorageParentDaoExtend;
import demo.simple.domain.FixedMaterialStorageParent;

public interface FixedMaterialStorageParentDao extends MongoDao<FixedMaterialStorageParent>, FixedMaterialStorageParentDaoExtend {
}
