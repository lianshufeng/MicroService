package com.github.microservice.auth.server.core.controller.common;

import com.github.microservice.auth.client.service.AuthResourcesNameService;
import com.github.microservice.auth.security.type.AuthType;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RequestMapping("common")
@RestController
public class CommonController {

    @Autowired
    private AuthResourcesNameService authResourcesNameService;

    @RequestMapping(value = "list", method = {RequestMethod.GET, RequestMethod.POST})
    public Object listAuthResourcesName(
            @ApiParam(name = "authType", value = "权限类型", example = "Enterprise") @RequestParam(defaultValue = "Enterprise") AuthType authType
            , @ApiIgnore Pageable pageable) {
        return authResourcesNameService.list(authType, pageable);
    }

}
