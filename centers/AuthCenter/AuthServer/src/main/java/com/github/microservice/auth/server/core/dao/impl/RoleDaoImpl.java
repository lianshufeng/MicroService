package com.github.microservice.auth.server.core.dao.impl;

import com.github.microservice.auth.client.model.RoleModel;
import com.github.microservice.auth.server.core.dao.EnterpriseDao;
import com.github.microservice.auth.server.core.dao.RoleDao;
import com.github.microservice.auth.server.core.dao.extend.RoleDaoExtend;
import com.github.microservice.auth.server.core.domain.Enterprise;
import com.github.microservice.auth.server.core.domain.Role;
import com.github.microservice.components.data.base.util.PageEntityUtil;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.github.microservice.components.data.mongo.mongo.util.EntityObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Set;

public class RoleDaoImpl implements RoleDaoExtend {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private EnterpriseDao enterpriseDao;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;


//    @Override
//    public Page<Role> list(RoleModel roleModel, Pageable pageable) {
//        //企业id
//        Enterprise enterprise = this.enterpriseDao.findTop1ById(roleModel.getEnterpriseId());
//        if (enterprise == null) {
//            return PageEntityUtil.buildEmptyPage(pageable);
//        }
//
//        //条件查询
//        Criteria criteria = Criteria.where("enterprise").is(enterprise);
//        criteria = EntityObjectUtil.buildCriteria(criteria, roleModel, EntityObjectUtil.CriteriaType.Like, "name", "remark");
//        criteria = EntityObjectUtil.buildCriteria(criteria, roleModel, EntityObjectUtil.CriteriaType.In, "auth", "identity");
//
//        return this.dbHelper.pages(Query.query(criteria), pageable, Role.class);
//    }

//    @Override
//    public List<Role> findByAuthName(String enterpriseId, String... authName) {
//        Enterprise enterprise = Enterprise.build(enterpriseId);
//
//        Query query = new Query();
//        query.addCriteria(Criteria.where("enterprise").is(enterprise).and("auth").in(Set.of(authName)));
//
//        return this.mongoTemplate.find(query, Role.class);
//    }

    @Override
    public List<Role> findByIdentity(String enterpriseId, String... identity) {
        Enterprise enterprise = Enterprise.build(enterpriseId);

        Query query = new Query();
        query.addCriteria(Criteria.where("enterprise").is(enterprise).and("identity").in(Set.of(identity)));

        return this.mongoTemplate.find(query, Role.class);
    }
}
