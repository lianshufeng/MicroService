package com.github.microservice.auth.server.core.controller.manager;

import com.github.microservice.auth.client.content.ResultContent;
import com.github.microservice.auth.server.core.model.UserLogModel;
import com.github.microservice.auth.server.core.service.local.UserLogServiceImpl;
import com.github.microservice.core.helper.ViewHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("manager/userlog")
public class UserLogManagerController {

    @Autowired
    private UserLogServiceImpl userLogService;

    @Autowired
    private ViewHelper viewHelper;

    @RequestMapping(value = "list", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultContent<Page<UserLogModel>> list(@PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultContent.buildContent(userLogService.list(pageable));
    }


    @RequestMapping(value = {"", "/"})
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("UserLog");
        modelAndView.addObject("script_set_baseUrl", viewHelper.getRemoteHost());
        return modelAndView;
    }


}
