package com.github.ms.demo.app.core.controller.manager;

import com.github.microservice.core.delegate.DelegateMapping;
import com.github.ms.demo.app.fegin.WebService;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manager/web/service")
public class WebServiceManagerController implements WebService {

    @Autowired
    @Delegate(types = WebService.class)
    private WebService webService;

}
