package com.github.ms.demo.app.core.service;

import com.github.microservice.core.util.result.InvokerResult;
import com.github.ms.demo.app.fegin.WebService;
import com.github.ms.demo.app.fegin.model.ObjData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WebServiceImpl implements WebService {

    @Override
    public long time() {
        return System.currentTimeMillis();
    }

    @Override
    public Map<String, Object> who(String name) {
        return Map.of("time", System.currentTimeMillis(), "name", name);
    }

    @Override
    public InvokerResult<Page<Object>> list(Pageable pageable, String name) {
        return InvokerResult.success(new PageImpl<Object>(List.of(name,"a", "b", "c"), pageable, 10));
    }

    @Override
    public InvokerResult<Object> obj(ObjData objData) {
        return InvokerResult.success(objData);
    }


}
