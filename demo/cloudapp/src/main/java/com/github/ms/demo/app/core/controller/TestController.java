package com.github.ms.demo.app.core.controller;

import com.github.ms.demo.app.core.ret.InvokerResult;
import com.github.ms.demo.app.core.service.TestService;
import com.github.ms.demo.app.fegin.model.ObjData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("time")
    public Object time() {
//        return Map.of("time", testService.time(), "state", "Success", "content", "test");
//        return Map.of("time", testService.time(), "state", "Success");
//        return Map.of("time", testService.time());
        return InvokerResult.notNull(System.currentTimeMillis());
    }

    @RequestMapping(value = "obj", consumes = MediaType.APPLICATION_JSON_VALUE)
    ObjData obj(@RequestBody ObjData objData) {
        return objData;
    }

}
