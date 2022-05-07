package com.github.microservice.auth.server.core.service.local;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.client.model.UserLoginLogModel;
import com.github.microservice.auth.client.service.UserLoginLogService;
import com.github.microservice.auth.server.core.dao.UserLoginLogDao;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.auth.server.core.domain.UserLoginLog;
import com.github.microservice.components.data.base.util.PageEntityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserLoginLogServiceImpl implements UserLoginLogService {

    @Autowired
    private UserLoginLogDao userLoginLogDao;

    @Override
    public ResultContent<Page<UserLoginLogModel>> list(String uid, Pageable pageable) {
        return ResultContent.buildContent(PageEntityUtil.toPageModel(this.userLoginLogDao.findByUser(User.build(uid), pageable), (it) -> {
            return toModel(it);
        }));
    }


    /**
     * 转换到模型
     *
     * @param userLoginLog
     * @return
     */
    public static UserLoginLogModel toModel(UserLoginLog userLoginLog) {
        if (userLoginLog == null) {
            return null;
        }
        UserLoginLogModel model = new UserLoginLogModel();
        BeanUtils.copyProperties(userLoginLog, model);
        model.setUid(userLoginLog.getUser().getId());
        return model;
    }

}
