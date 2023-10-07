package demo.simple.dao;

import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;
import demo.simple.dao.extend.FixedMaterialStorageTrackRecordDaoExtend;
import demo.simple.domain.FixedMaterialStorageTrackRecord;

public interface FixedMaterialStorageTrackRecordDao extends MongoDao<FixedMaterialStorageTrackRecord>, FixedMaterialStorageTrackRecordDaoExtend {
}
