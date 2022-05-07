package com.github.microservice.centers.experiment.query.core.service;

import com.github.microservice.centers.experiment.query.core.dao.ExperimentDao;
import com.github.microservice.centers.experiment.query.core.dao.UserGroupDao;
import com.github.microservice.centers.experiment.query.core.domain.Experiment;
import com.github.microservice.centers.experiment.query.core.domain.UserGroup;
import com.github.microservice.centers.experiment.query.core.model.ExperimentModel;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ExperimentService {

    @Autowired
    private ExperimentDao experimentDao;

    @Autowired
    private UserGroupDao userGroupDao;

    @Autowired
    private DBHelper dbHelper;

    /**
     * 更新查询实验环境
     *
     * @param experimentModel
     * @return
     */
    @Transactional
    public ExperimentModel upsert(ExperimentModel experimentModel) {
        log.info("upsert - {}", experimentModel);
        Assert.hasText(experimentModel.getName(), "灰度名不能为空");
        //数据库中查询，没有则新增
        Experiment experiment = this.experimentDao.findTop1ByName(experimentModel.getName());
        if (experiment == null) {
            experiment = new Experiment();
            experiment.setName(experimentModel.getName());
        }
        //拷贝对象
        BeanUtils.copyProperties(experimentModel, experiment, "name", "mapping");

        //将map的key与value都转换为小写
        final Map<String, String> mapping = new HashMap<>();
        experimentModel.getMapping().entrySet().forEach(entry -> {
            mapping.put(entry.getKey().toLowerCase(), entry.getValue().toLowerCase());
        });
        experiment.setMapping(mapping);


        this.dbHelper.saveTime(experiment);
        this.experimentDao.save(experiment);
        return toModel(experiment);
    }


    public ExperimentModel[] queryByUidAndServiceName(String uid, String serviceName) {
        return this.experimentDao.queryByUidAndServiceName(uid, serviceName).stream()
                //转换为灰度模型
                .map(this::toModel).toArray(ExperimentModel[]::new);
    }


    /**
     * 通过实验名查询实验环境
     *
     * @param name
     * @return
     */
    public ExperimentModel get(String name) {
        return toModel(this.experimentDao.findTop1ByName(name));
    }

    /**
     * 删除一个实验环境
     *
     * @param name
     * @return
     */
    public Boolean del(String name) {
        return this.experimentDao.removeByName(name) > 0;
    }

    /**
     * 分页查询实验环境列表
     *
     * @param pageable
     * @return
     */
    public Page<ExperimentModel> list(Pageable pageable) {
        return PageEntityUtil.concurrent2PageModel(this.experimentDao.list(pageable), this::toModel);
    }


    protected ExperimentModel toModel(Experiment experiment) {
        if (experiment == null) {
            return null;
        }
        ExperimentModel model = new ExperimentModel();
        BeanUtils.copyProperties(experiment, model);
        return model;
    }
}
