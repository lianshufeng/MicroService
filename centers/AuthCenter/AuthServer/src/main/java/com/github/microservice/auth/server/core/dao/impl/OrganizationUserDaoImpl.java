package com.github.microservice.auth.server.core.dao.impl;

import com.github.microservice.auth.server.core.dao.extend.OrganizationUserDaoExtend;
import com.github.microservice.auth.server.core.domain.OrganizationUser;
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

public class OrganizationUserDaoImpl implements OrganizationUserDaoExtend {

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<OrganizationUser> query(String mql, String[] fields, Pageable pageable) {
        return this.dbHelper.queryByMql(
                QueryModel.builder().mql(mql).fields(Set.of(fields)).build(),
                pageable,
                OrganizationUser.class
        );
    }

    @Override
    public long update(List<OrganizationUser> organizationUsers) {
        if (organizationUsers.size() == 0) {
            return 0;
        }
        BulkOperations bulkOperations = this.mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, OrganizationUser.class);
        organizationUsers.forEach((organizationUser) -> {
            Query query = new Query(
                    Criteria.where("organization").is(organizationUser.getOrganization())
                            .and("user").is(organizationUser.getUser())
            );

            Update update = new Update();

            EntityObjectUtil.entity2Update(organizationUser, update, "organization", "user");

            update.setOnInsert("organization", organizationUser.getOrganization());
            update.setOnInsert("user", organizationUser.getUser());
            this.dbHelper.updateTime(update);
            bulkOperations.upsert(query, update);
        });
        return bulkOperations.execute().getModifiedCount();
    }

    @Override
    public Page<OrganizationUser> affiliatesOrganization(String uid, Pageable pageable) {
        return this.dbHelper.pages(
                Query.query(
                        Criteria
                                .where("user").is(User.build(uid))
                                .and("roleId.0").is(Map.of("$exists", true))
                ),
                pageable,
                OrganizationUser.class
        );
    }
}
