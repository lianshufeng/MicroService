package com.github.microservice.auth.server.core.service.local;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.content.ResultState;
import com.github.microservice.auth.client.helper.AuthEventStreamHelper;
import com.github.microservice.auth.client.model.EnterpriseModel;
import com.github.microservice.auth.client.model.stream.EnterpriseStreamModel;
import com.github.microservice.auth.client.service.EnterpriseService;
import com.github.microservice.auth.client.type.AuthEventType;
import com.github.microservice.auth.client.type.AuthStreamType;
import com.github.microservice.auth.security.type.AuthType;
import com.github.microservice.auth.server.core.dao.EnterpriseDao;
import com.github.microservice.auth.server.core.domain.Enterprise;
import com.github.microservice.components.data.base.util.PageEntityUtil;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.github.microservice.core.util.bean.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    public static final AuthStreamType StreamType = AuthStreamType.Enterprise;

    @Autowired
    private EnterpriseDao enterpriseDao;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private AuthEventStreamHelper authStreamHelper;


    @Override
    public ResultContent<Void> existsByName(String epId) {
        if (!StringUtils.hasText(epId)) {
            return ResultContent.build(ResultState.EnterpriseNotNull);
        }
        return ResultContent.build(this.enterpriseDao.existsByName(epId));
    }

    @Override
    public ResultContent<EnterpriseModel> get(String epId) {
        return ResultContent.buildContent(toModel(this.enterpriseDao.findTop1ById(epId)));
    }

    @Override
    public ResultContent<EnterpriseModel> list(AuthType authType, Pageable pageable) {
        return ResultContent.buildContent(PageEntityUtil.toPageModel(this.enterpriseDao.findByAuthType(authType, pageable), (it) -> {
            return toModel(it);
        }));
    }

    @Override
    public ResultContent<String> add(EnterpriseModel enterpriseModel) {
        Assert.hasText(enterpriseModel.getName(), "企业名不能为空");
        Assert.notNull(enterpriseModel.getAuthType(), "企业权限类型不能为空");

        Enterprise enterprise = new Enterprise();
        BeanUtils.copyProperties(enterpriseModel, enterprise, "id");
        this.dbHelper.saveTime(enterprise);
        this.enterpriseDao.save(enterprise);
        this.authStreamHelper.publish(new EnterpriseStreamModel(AuthEventType.Add, enterprise.getId()));
        return ResultContent.buildContent(enterprise.getId());
    }

    /**
     * 修改
     *
     * @param enterpriseModel
     * @return
     */
    public ResultContent<String> modify(EnterpriseModel enterpriseModel) {
        Enterprise enterprise = this.enterpriseDao.findTop1ById(enterpriseModel.getId());
        if (enterprise == null) {
            return ResultContent.build(ResultState.EnterpriseNotExist);
        }
        Set<String> ignoreProperties = new HashSet<>();
        BeanUtil.getNullPropertyNames(enterpriseModel, ignoreProperties);
        BeanUtils.copyProperties(enterpriseModel, enterprise, ignoreProperties.toArray(new String[0]));
        this.dbHelper.saveTime(enterprise);
        this.enterpriseDao.save(enterprise);
        this.authStreamHelper.publish(new EnterpriseStreamModel(AuthEventType.Modify, enterprise.getId()));
        return ResultContent.buildContent(enterprise.getId());
    }


    @Override
    public ResultContent<String> update(EnterpriseModel enterpriseModel) {
        return StringUtils.hasText(enterpriseModel.getId()) ? modify(enterpriseModel) : add(enterpriseModel);
    }


    @Override
    public ResultContent<Void> disable(String epId) {
        boolean success = this.enterpriseDao.disable(epId, true);
        if (success) {
            this.authStreamHelper.publish(new EnterpriseStreamModel(AuthEventType.Disable, epId));
        }
        return ResultContent.build(success);
    }

    @Override
    public ResultContent<Void> enable(String epId) {
        boolean success = this.enterpriseDao.disable(epId, false);
        if (success) {
            this.authStreamHelper.publish(new EnterpriseStreamModel(AuthEventType.Enable, epId));
        }
        return ResultContent.build(success);
    }


    /**
     * 转换到企业模型
     *
     * @param enterprise
     * @return
     */
    public static EnterpriseModel toModel(Enterprise enterprise) {
        if (enterprise == null) {
            return null;
        }
        EnterpriseModel enterpriseModel = new EnterpriseModel();
        BeanUtils.copyProperties(enterprise, enterpriseModel);
        return enterpriseModel;
    }


}
