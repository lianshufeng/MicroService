package demo.simple.dao;

import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;
import demo.simple.domain.UserInfo;

public interface UserInfoDao extends MongoDao<UserInfo> {

}
