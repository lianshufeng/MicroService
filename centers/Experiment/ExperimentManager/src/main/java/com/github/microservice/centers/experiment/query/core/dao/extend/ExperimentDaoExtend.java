package com.github.microservice.centers.experiment.query.core.dao.extend;

import com.github.microservice.centers.experiment.query.core.domain.Experiment;

import java.util.List;

public interface ExperimentDaoExtend {


    List<Experiment> queryByUidAndServiceName(String uid, String serviceName);

}
