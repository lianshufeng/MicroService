package com.github.microservice.components.data.mongo.orm.tree.dao.impl;

import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.github.microservice.components.data.mongo.orm.tree.dao.TrackRecordEntityExtend;
import com.github.microservice.components.data.mongo.orm.tree.domain.TrackRecordEntity;
import com.github.microservice.components.data.mongo.token.service.ResourceTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TrackRecordEntityImpl<T extends TrackRecordEntity> implements TrackRecordEntityExtend {

    @Autowired
    private ResourceTokenService resourceTokenService;

    @Autowired
    private DBHelper dbHelper;
}
