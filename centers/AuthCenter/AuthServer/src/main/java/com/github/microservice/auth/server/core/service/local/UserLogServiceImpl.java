package com.github.microservice.auth.server.core.service.local;

import com.github.microservice.auth.server.core.dao.UserLogDao;
import com.github.microservice.auth.server.core.domain.UserLog;
import com.github.microservice.auth.server.core.model.UserLogModel;
import com.github.microservice.components.data.base.util.PageEntityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UserLogServiceImpl {

    @Autowired
    private UserLogDao userLogDao;


    /**
     * 分页查询
     * @param pageable
     * @return
     */
    public Page<UserLogModel> list(Pageable pageable) {
        return PageEntityUtil.concurrent2PageModel(this.userLogDao.list(pageable), this::toModel);
    }


    private UserLogModel toModel(UserLog userLog) {
        UserLogModel userLogModel = new UserLogModel();
        BeanUtils.copyProperties(userLog, userLogModel);
        return userLogModel;
    }


}
