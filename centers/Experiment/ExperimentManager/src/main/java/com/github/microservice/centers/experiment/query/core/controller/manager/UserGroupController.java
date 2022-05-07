package com.github.microservice.centers.experiment.query.core.controller.manager;

import com.github.microservice.centers.experiment.query.core.constant.ExampleConstant;
import com.github.microservice.centers.experiment.query.core.model.ExperimentModel;
import com.github.microservice.centers.experiment.query.core.model.UserGroupModel;
import com.github.microservice.centers.experiment.query.core.result.ResultContent;
import com.github.microservice.centers.experiment.query.core.service.ExperimentService;
import com.github.microservice.centers.experiment.query.core.service.UserGroupService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("manager/userGroup")
public class UserGroupController {

    @Autowired
    private UserGroupService userGroupService;


    @ApiOperation(value = "增加/修改一个用户组", notes = "增加/修改一个用户组", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "upsert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultContent<ExperimentModel> upsert(@RequestBody UserGroupModel userGroupModel) {
        return ResultContent.buildContent(userGroupService.upsert(userGroupModel));
    }


    @ApiOperation(value = "删除一个用户组", notes = "删除一个用户组", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "del", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    public ResultContent<Void> del(@ApiParam(name = "name", value = "实验名", example = ExampleConstant.UserGroupName) @RequestParam(value = "name") String name) {
        return ResultContent.build(userGroupService.del(name));
    }


    @ApiOperation(value = "查询一个用户组", notes = "查询一个用户组", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "get", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    public ResultContent<ExperimentModel> get(@ApiParam(name = "name", value = "实验名", example = ExampleConstant.UserGroupName) @RequestParam(value = "name") String name) {
        return ResultContent.buildContent(userGroupService.get(name));
    }

    @ApiOperation(value = "分页查询用户组列表", notes = "分页查询用户组列表", consumes = MediaType.ALL_VALUE)
    @RequestMapping(value = "list", consumes = {MediaType.ALL_VALUE}, method = RequestMethod.POST)
    public ResultContent<Page<UserGroupModel>> list(
            //分页 , 参数无用，仅为api文档显示
            @ApiParam(name = "page", value = "页码", example = "0") @RequestParam(value = "page") String page,
            //
            @ApiParam(name = "size", value = "每页显示条数", example = "5") @RequestParam(value = "size") String size,
            // 用户id，不为空则精准查询
            @ApiParam(name = "uid", value = "用户id", example = "") @RequestParam(value = "uid", required = false) String uid,
            //分页模型
            @ApiParam(name = "sort", value = "排序", example = "createTime,desc") @RequestParam(value = "sort") String sort, @ApiIgnore @PageableDefault Pageable pageable) {
        return ResultContent.buildContent(userGroupService.list(uid, pageable));
    }


}
