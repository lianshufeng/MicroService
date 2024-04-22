package com.github.ms.demo.app.fegin;

import com.github.microservice.core.util.result.InvokerResult;
import com.github.ms.demo.app.fegin.model.ObjData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@FeignClient(name = "cloudapp/manager/web/service")
public interface WebService {


    @RequestMapping(value = "time", method = RequestMethod.GET)
    long time();

    @RequestMapping(value = "who")
    Map<String, Object> who(@RequestParam("name") String name);


    @RequestMapping(value = "list")
    InvokerResult<Page<Object>> list(Pageable pageable, @RequestParam("name") String name);


    //    @RequestMapping(value = "obj", consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "obj", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    InvokerResult<Object> obj(@RequestBody ObjData objData);

}
