package com.github.microservice.centers.experiment.query.core.dao;

import com.github.microservice.centers.experiment.query.core.dao.extend.ExperimentDaoExtend;
import com.github.microservice.centers.experiment.query.core.domain.Experiment;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

public interface ExperimentDao extends MongoDao<Experiment>, ExperimentDaoExtend {

    Experiment findTop1ByName(String name);


    Long removeByName(String name);


    @Query("{}")
    Page<Experiment> list(Pageable pageable);

    /**
     *  灰度环境是否存在
     */

}
