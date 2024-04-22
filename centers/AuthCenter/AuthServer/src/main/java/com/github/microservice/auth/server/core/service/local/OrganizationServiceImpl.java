package com.github.microservice.auth.server.core.service.local;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.content.ResultState;
import com.github.microservice.auth.client.helper.AuthEventStreamHelper;
import com.github.microservice.auth.client.model.OrganizationModel;
import com.github.microservice.auth.client.model.stream.OrganizationStreamModel;
import com.github.microservice.auth.client.service.OrganizationService;
import com.github.microservice.auth.client.type.AuthEventType;
import com.github.microservice.auth.client.type.AuthStreamType;
import com.github.microservice.auth.security.type.AuthType;
import com.github.microservice.auth.server.core.dao.OrganizationDao;
import com.github.microservice.auth.server.core.domain.Organization;
import com.github.microservice.components.data.base.util.PageEntityUtil;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import com.github.microservice.core.util.bean.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Service
@Primary
public class OrganizationServiceImpl implements OrganizationService {

    public static final AuthStreamType StreamType = AuthStreamType.Organization;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private AuthEventStreamHelper authStreamHelper;


    @Override
    public ResultContent<Void> existsByName(String name) {
        if (!StringUtils.hasText(name)) {
            return ResultContent.build(ResultState.OrganizationNotExist);
        }
        return ResultContent.build(this.organizationDao.existsByName(name));
    }

    @Override
    public ResultContent<OrganizationModel> get(String oid) {
        return ResultContent.buildContent(toModel(this.organizationDao.findTop1ById(oid)));
    }

    @Override
    public ResultContent<OrganizationModel> findByName(String name) {
        return ResultContent.buildContent(toModel(this.organizationDao.findByName(name)));
    }

    @Override
    public ResultContent<OrganizationModel> list(AuthType authType, Pageable pageable) {
        return ResultContent.buildContent(PageEntityUtil.toPageModel(this.organizationDao.findByAuthType(authType, pageable), (it) -> {
            return toModel(it);
        }));
    }

    @Override
    public ResultContent<String> add(OrganizationModel organizationModel) {
        Assert.hasText(organizationModel.getName(), "机构名不能为空");
        Assert.notNull(organizationModel.getAuthType(), "机构权限类型不能为空");

        //不允许操作更新的机构类型
        if (!organizationModel.getAuthType().isOrganization()) {
            return ResultContent.build(ResultState.OrganizationAuthTypeDoNotUpdate);
        }

        //判断单例的权限类型
        if (organizationModel.getAuthType().isSingle() && this.organizationDao.existsByAuthType(organizationModel.getAuthType())) {
            return ResultContent.build(ResultState.AuthTypeExist);
        }

        if (this.organizationDao.existsByName(organizationModel.getName())) {
            return ResultContent.build(ResultState.OrganizationNameExist);
        }


        Organization organization = new Organization();
        BeanUtils.copyProperties(organizationModel, organization, "id");
        this.dbHelper.saveTime(organization);
        this.organizationDao.insert(organization);
        this.authStreamHelper.publish(new OrganizationStreamModel(AuthEventType.Add, organization.getId()));
        return ResultContent.buildContent(organization.getId());
    }

    /**
     * 修改
     *
     * @param organizationModel
     * @return
     */
    public ResultContent<String> modify(OrganizationModel organizationModel) {
        Organization organization = this.organizationDao.findTop1ById(organizationModel.getId());
        if (organization == null) {
            return ResultContent.build(ResultState.OrganizationNotExist);
        }

        //机构类型不允许修改
        if (organizationModel.getAuthType() != null && organization.getAuthType() != organizationModel.getAuthType()) {
            return ResultContent.build(ResultState.OrganizationAuthTypeDoNotUpdate);
        }

        //修改机构名
        if (StringUtils.hasText(organizationModel.getName()) && !organization.getName().equals(organizationModel.getName()) && this.organizationDao.existsByName(organizationModel.getName())) {
            return ResultContent.build(ResultState.OrganizationNameExist);
        }

        Set<String> ignoreProperties = new HashSet<>() {{
            add("authType");
            add("id");
        }};
        BeanUtil.getNullPropertyNames(organizationModel, ignoreProperties);
        BeanUtils.copyProperties(organizationModel, organization, ignoreProperties.toArray(new String[0]));


        this.dbHelper.saveTime(organization);
        this.organizationDao.save(organization);
        this.authStreamHelper.publish(new OrganizationStreamModel(AuthEventType.Modify, organization.getId()));
        return ResultContent.buildContent(organization.getId());
    }


    @Override
    public ResultContent<String> update(OrganizationModel organizationModel) {
        Assert.hasText(organizationModel.getId(), "机构ID不能为空");
        return modify(organizationModel);
    }


    @Override
    public ResultContent<Void> disable(String epId) {
        boolean success = this.organizationDao.disable(epId, true);
        if (success) {
            this.authStreamHelper.publish(new OrganizationStreamModel(AuthEventType.Disable, epId));
        }
        return ResultContent.build(success);
    }

    @Override
    public ResultContent<Void> enable(String epId) {
        boolean success = this.organizationDao.disable(epId, false);
        if (success) {
            this.authStreamHelper.publish(new OrganizationStreamModel(AuthEventType.Enable, epId));
        }
        return ResultContent.build(success);
    }


    /**
     * 转换到企业模型
     *
     * @param organization
     * @return
     */
    public static OrganizationModel toModel(Organization organization) {
        if (organization == null) {
            return null;
        }
        OrganizationModel organizationModel = new OrganizationModel();
        BeanUtils.copyProperties(organization, organizationModel);
        return organizationModel;
    }


}
