package demo.simple.controller;

import com.github.microservice.components.data.base.data.DataCleanManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataCleanController {

    @Autowired
    private DataCleanManager dataCleanManager;


    @RequestMapping("dc")
    public Object dc(String taskName) {
        return dataCleanManager.execute(taskName, true);
    }


}
