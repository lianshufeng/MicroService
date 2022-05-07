package com.github.microservice.auth.server.core.service.local;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.content.ResultState;
import com.github.microservice.auth.client.model.AuthResourcesNameModel;
import com.github.microservice.auth.client.service.AuthResourcesNameService;
import com.github.microservice.auth.security.type.AuthType;
import com.github.microservice.auth.server.core.dao.AuthResourcesNameDao;
import com.github.microservice.auth.server.core.domain.AuthResourcesName;
import com.github.microservice.components.data.base.util.PageEntityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class AuthResourcesNameServiceImpl implements AuthResourcesNameService {

    @Autowired
    private AuthResourcesNameDao authResourcesNameDao;


    @Override
    public ResultContent<Integer> update(AuthResourcesNameModel[] authResourcesNameModel) {
        if (authResourcesNameModel == null || authResourcesNameModel.length == 0) {
            return ResultContent.build(ResultState.Fail);
        }
        return ResultContent.buildContent(authResourcesNameDao.update(Arrays.stream(authResourcesNameModel)
                .filter((it) -> {
                    return StringUtils.hasText(it.getName()) && it.getResourceType() != null;
                })
                .map((it) -> {
                    AuthResourcesName authResourcesName = new AuthResourcesName();
                    BeanUtils.copyProperties(it, authResourcesName);
                    return authResourcesName;
                }).collect(Collectors.toList()).toArray(new AuthResourcesName[0])));
    }

    @Override
    public ResultContent<Page<AuthResourcesNameModel>> list(AuthType authType, Pageable pageable) {
        return ResultContent.buildContent(PageEntityUtil.toPageModel(this.authResourcesNameDao.findByAuthType(authType, pageable), (it) -> {
            return toModel(it);
        }));
    }

    private AuthResourcesNameModel toModel(AuthResourcesName authResourcesName) {
        AuthResourcesNameModel authResourcesNameModel = new AuthResourcesNameModel();
        BeanUtils.copyProperties(authResourcesName, authResourcesNameModel);
        return authResourcesNameModel;
    }


}
