package com.github.microservice.auth.server.core.stream;

import com.github.microservice.app.stream.StreamConsumer;
import com.github.microservice.auth.security.model.UserLogModel;
import com.github.microservice.auth.server.core.conf.UserLogConf;
import com.github.microservice.auth.server.core.dao.UserLogDao;
import com.github.microservice.auth.server.core.domain.UserLog;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 用户日志的消费者
 */
@Component
public class UserLogStreamConsumer extends StreamConsumer<UserLogModel> {

    @Autowired
    private UserLogDao userLogDao;

    @Autowired
    private DBHelper dbHelper;

    @Autowired
    private UserLogConf userLogConf;


    @Override
    public void accept(UserLogModel userLogModel) {
        UserLog userLog = new UserLog();
        BeanUtils.copyProperties(userLogModel, userLog);
        userLog.setTTL(new Date(this.dbHelper.getTime() + userLogConf.getTimeOut()));
        userLogDao.save(userLog);
    }
}
