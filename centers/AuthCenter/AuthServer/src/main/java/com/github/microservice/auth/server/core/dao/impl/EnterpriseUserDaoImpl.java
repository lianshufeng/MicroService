package com.github.microservice.auth.server.core.dao.impl;

import com.github.microservice.auth.server.core.dao.extend.EnterpriseUserDaoExtend;
import com.github.microservice.auth.server.core.domain.EnterpriseUser;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.github.microservice.components.data.mongo.mongo.model.QueryModel;
import com.github.microservice.components.data.mongo.mongo.util.EntityObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnterpriseUserDaoImpl implements EnterpriseUserDaoExtend {

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<EnterpriseUser> query(String mql, String[] fields, Pageable pageable) {
        return this.dbHelper.queryByMql(
                QueryModel.builder().mql(mql).fields(Set.of(fields)).build(),
                pageable,
                EnterpriseUser.class
        );
    }

    @Override
    public long update(List<EnterpriseUser> enterpriseUsers) {
        if (enterpriseUsers.size() == 0) {
            return 0;
        }
        BulkOperations bulkOperations = this.mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, EnterpriseUser.class);
        enterpriseUsers.forEach((enterpriseUser) -> {
            Query query = new Query(
                    Criteria.where("enterprise").is(enterpriseUser.getEnterprise())
                            .and("user").is(enterpriseUser.getUser())
            );

            Update update = new Update();

            EntityObjectUtil.entity2Update(enterpriseUser, update, "enterprise", "user");

            update.setOnInsert("enterprise", enterpriseUser.getEnterprise());
            update.setOnInsert("user", enterpriseUser.getUser());
            this.dbHelper.updateTime(update);
            bulkOperations.upsert(query, update);
        });
        return bulkOperations.execute().getModifiedCount();
    }

    @Override
    public Page<EnterpriseUser> affiliatesEnterprise(String uid, Pageable pageable) {
        return this.dbHelper.pages(
                Query.query(
                        Criteria
                                .where("user").is(User.build(uid))
                                .and("roleId.0").is(Map.of("$exists", true))
                ),
                pageable,
                EnterpriseUser.class
        );
    }
}
