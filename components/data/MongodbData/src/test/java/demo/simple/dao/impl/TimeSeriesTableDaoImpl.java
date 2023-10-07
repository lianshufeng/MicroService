package demo.simple.dao.impl;

import demo.simple.dao.TimeSeriesTableDao;
import demo.simple.domain.TimeSeriesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TimeSeriesTableDaoImpl implements TimeSeriesTableDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private void createCollection(ApplicationContext applicationContext) {
        if (!mongoTemplate.collectionExists(TimeSeriesTable.class)) {
            mongoTemplate.createCollection(TimeSeriesTable.class);
        }
    }


}
