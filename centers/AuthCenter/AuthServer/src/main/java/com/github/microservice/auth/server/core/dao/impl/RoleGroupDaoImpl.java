package com.github.microservice.auth.server.core.dao.impl;

import com.github.microservice.auth.server.core.dao.RoleDao;
import com.github.microservice.auth.server.core.dao.extend.RoleGroupDaoExtend;
import com.github.microservice.auth.server.core.domain.Role;
import com.github.microservice.auth.server.core.domain.RoleGroup;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.mongodb.DBRef;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.stream.Collectors;

public class RoleGroupDaoImpl implements RoleGroupDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DBHelper dbHelper;


    @Override
    public boolean removeRole(RoleGroup roleGroup, Collection<Role> roles) {
        final String roleCollectionName = mongoTemplate.getCollectionName(Role.class);
        Update update = new Update();
        //重新查询数据库，仅查询出存在的角色
        update.pullAll("roles",
                roles
                        .stream()
                        .map(it -> new DBRef(roleCollectionName, new ObjectId(it.getId())))
                        .collect(Collectors.toSet())
                        .toArray(new DBRef[0])
        );
        this.dbHelper.updateTime(update);
        return this.mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(roleGroup.getId())),
                update,
                RoleGroup.class
        ).getModifiedCount() > 0;
    }

    @Override
    public boolean addRole(RoleGroup roleGroup, Collection<Role> roles) {
        final String roleCollectionName = mongoTemplate.getCollectionName(Role.class);
        Update update = new Update();
        //重新查询数据库，仅查询出存在的角色
        update.addToSet("roles").each(
                this.roleDao
                        .findByIdIn(roles.stream().map(it -> it.getId()).collect(Collectors.toSet()))
                        .stream()
                        .map((role) -> {
                            return new DBRef(roleCollectionName, new ObjectId(role.getId()));
                        })
                        .collect(Collectors.toSet())
                        .toArray(new DBRef[0])
        );
        this.dbHelper.updateTime(update);
        return this.mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(roleGroup.getId())),
                update,
                RoleGroup.class
        ).getModifiedCount() > 0;
    }
}
