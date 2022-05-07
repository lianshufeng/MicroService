package com.github.microservice.helper.core.controller;

import com.github.microservice.helper.core.helper.ProjectHelper;
import com.github.microservice.helper.core.model.ProjectTask;
import com.github.microservice.helper.core.model.ProjectTaskResp;
import com.github.microservice.helper.core.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("task")
public class TaskController {


    @Autowired
    private ProjectHelper projectHelper;

    @Autowired
    private TaskService taskService;

    /**
     * 执行项目任务
     *
     * @return
     */
    @RequestMapping("add")
    public Object task(ProjectTask task) {
        this.projectHelper.reload();
        if (task.getProjectName() == null || task.getProjectName().length == 0) {
            task.setProjectName(this.projectHelper.getItems().values().stream().filter((it) -> {
                return it != null && it.getBuild() != null && it.getBuild();
            }).map((it) -> {
                return it.getName();
            }).collect(Collectors.toList()).toArray(new String[0]));
        }
        return ProjectTaskResp.builder().projectName(this.taskService.add(task)).createTime(System.currentTimeMillis()).build();
    }


    /**
     * 任务列表
     *
     * @return
     */
    @RequestMapping("list")
    public Object task() {
        return this.taskService.list();
    }
}
