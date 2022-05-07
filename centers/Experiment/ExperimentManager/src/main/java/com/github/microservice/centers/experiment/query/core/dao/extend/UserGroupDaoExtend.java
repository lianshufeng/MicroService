package com.github.microservice.centers.experiment.query.core.dao.extend;

import com.github.microservice.centers.experiment.query.core.domain.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserGroupDaoExtend {


    Page<UserGroup> list(String uid, Pageable pageable);
}
