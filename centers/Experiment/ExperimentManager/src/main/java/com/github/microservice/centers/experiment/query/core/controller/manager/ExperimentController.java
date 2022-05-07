package com.github.microservice.centers.experiment.query.core.controller.manager;

import com.github.microservice.centers.experiment.query.core.constant.ExampleConstant;
import com.github.microservice.centers.experiment.query.core.model.ExperimentModel;
import com.github.microservice.centers.experiment.query.core.result.ResultContent;
import com.github.microservice.centers.experiment.query.core.service.ExperimentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("manager/experiment")
public class ExperimentController {

    @Autowired
    private ExperimentService experimentService;


    @ApiOperation(value = "增加/修改一个灰度环境", notes = "增加/修改一个灰度环境", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "upsert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultContent<ExperimentModel> upsert(@RequestBody ExperimentModel experimentModel) {
        return ResultContent.buildContent(experimentService.upsert(experimentModel));
    }


    @ApiOperation(value = "删除一个灰度环境", notes = "删除一个灰度环境", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "del", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    public ResultContent<Void> del(@ApiParam(name = "name", value = "实验名", example = ExampleConstant.ExperimentName) @RequestParam(value = "name") String name) {
        return ResultContent.build(experimentService.del(name));
    }


    @ApiOperation(value = "查询一个灰度环境", notes = "查询一个灰度环境", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "get", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    public ResultContent<ExperimentModel> get(@ApiParam(name = "name", value = "实验名", example = ExampleConstant.ExperimentName) @RequestParam(value = "name") String name) {
        return ResultContent.buildContent(experimentService.get(name));
    }

    @ApiOperation(value = "分页查询灰度环境列表", notes = "分页查询灰度环境列表", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "list", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    public ResultContent<ExperimentModel> list(
            //分页 , 参数无用，仅为api文档显示
            @ApiParam(name = "page", value = "页码", example = "0") @RequestParam(value = "page") String page,
            //
            @ApiParam(name = "size", value = "每页显示条数", example = "20") @RequestParam(value = "size") String size,
            //
            @ApiParam(name = "sort", value = "排序", example = "createTime,desc") @RequestParam(value = "sort") String sort, @ApiIgnore @PageableDefault Pageable pageable) {
        return ResultContent.buildContent(experimentService.list(pageable));
    }


    @ApiOperation(value = "通过用户id查询实验环境", notes = "通过用户id查询实验环境", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "queryByUidAndServiceName", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.GET)
    public ResultContent<ExperimentModel[]> queryByUidAndServiceName(
            // 用户id
            @ApiParam(name = "uid", value = "用户id", example = ExampleConstant.UserId) @RequestParam(value = "uid") String uid,
            // 业务名
            @ApiParam(name = "serviceName", value = "业务名", example = ExampleConstant.service1) @RequestParam(value = "serviceName") String serviceName
    ) {
        return ResultContent.buildContent(experimentService.queryByUidAndServiceName(uid, serviceName));
    }


}
