package com.github.microservice.auth.server.core.controller.common;

import com.github.microservice.auth.security.type.AuthType;
import com.github.microservice.auth.server.core.service.local.AuthResourcesNameServiceImpl;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("common")
@RestController
public class CommonController {

    @Autowired
    private AuthResourcesNameServiceImpl authResourcesNameService;

    @RequestMapping(value = "list", method = {RequestMethod.GET, RequestMethod.POST})
    public Object listAuthResourcesName(
            @Parameter(name = "authType", description = "权限类型", example = "Enterprise") @RequestParam(defaultValue = "Enterprise", value = "authType") AuthType authType,
            @Parameter(hidden = true) Pageable pageable) {
        return authResourcesNameService.list(authType, pageable);
    }

}
