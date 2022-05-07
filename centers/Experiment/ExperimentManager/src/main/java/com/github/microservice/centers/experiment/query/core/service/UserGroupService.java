package com.github.microservice.centers.experiment.query.core.service;

import com.github.microservice.centers.experiment.query.core.dao.ExperimentDao;
import com.github.microservice.centers.experiment.query.core.dao.UserGroupDao;
import com.github.microservice.centers.experiment.query.core.domain.Experiment;
import com.github.microservice.centers.experiment.query.core.domain.UserGroup;
import com.github.microservice.centers.experiment.query.core.model.ExperimentModel;
import com.github.microservice.centers.experiment.query.core.model.UserGroupModel;
import com.github.microservice.components.data.base.util.PageEntityUtil;
import com.github.microservice.components.data.mongo.mongo.helper.DBHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Optional;

@Slf4j
@Service
public class UserGroupService {

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private ExperimentDao experimentDao;

    @Autowired
    private UserGroupDao userGroupDao;

    @Autowired
    private DBHelper dbHelper;

    /**
     * 更新查询实验环境
     *
     * @return
     */
    @Transactional
    public UserGroupModel upsert(UserGroupModel userGroupModel) {
        log.info("upsert - {}", userGroupModel);
        Assert.hasText(userGroupModel.getName(), "用户组名不能为空");
        //取出当前的环境模型
        final ExperimentModel experimentModel = userGroupModel.getExperiment();
        Assert.notNull(experimentModel, "灰度环境不能为空");
        Assert.notNull(experimentModel.getName(), "灰度环境环境名不能为空");
        final Experiment experiment = this.experimentDao.findTop1ByName(experimentModel.getName());
        Assert.notNull(experiment, "灰度环境不存在");

        //数据库中查询，没有则新增
        UserGroup userGroup = this.userGroupDao.findTop1ByName(userGroupModel.getName());
        if (userGroup == null) {
            userGroup = new UserGroup();
            userGroup.setName(userGroupModel.getName());
        }
        //拷贝对象
        BeanUtils.copyProperties(userGroupModel, userGroup, "name", "experiment");
        userGroup.setExperiment(experiment);

        this.dbHelper.saveTime(userGroup);
        this.userGroupDao.save(userGroup);
        return toModel(userGroup);
    }


    /**
     * 通过实验名查询实验环境
     *
     * @param name
     * @return
     */
    public UserGroupModel get(String name) {
        return toModel(this.userGroupDao.findTop1ByName(name));
    }

    /**
     * 删除一个实验环境
     *
     * @param name
     * @return
     */
    public Boolean del(String name) {
        return this.userGroupDao.removeByName(name) > 0;
    }

    /**
     * 分页查询实验环境列表
     *
     * @param pageable
     * @return
     */
    public Page<UserGroupModel> list(String uid, Pageable pageable) {
        return PageEntityUtil.concurrent2PageModel(this.userGroupDao.list(uid, pageable), this::toModel);
    }


    private UserGroupModel toModel(UserGroup userGroup) {
        if (userGroup == null) {
            return null;
        }
        UserGroupModel model = new UserGroupModel();
        BeanUtils.copyProperties(userGroup, model, "experiment");
        //灰度环境的转换
        model.setExperiment(this.experimentService.toModel(userGroup.getExperiment()));
        return model;
    }

}
