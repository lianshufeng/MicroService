package com.github.microservice.centers.experiment.query.core.dao;

import com.github.microservice.centers.experiment.query.core.dao.extend.UserGroupDaoExtend;
import com.github.microservice.centers.experiment.query.core.domain.UserGroup;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

public interface UserGroupDao extends MongoDao<UserGroup>, UserGroupDaoExtend {

    UserGroup findTop1ByName(String name);


    Long removeByName(String name);

}
