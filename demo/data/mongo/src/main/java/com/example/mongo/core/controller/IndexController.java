package com.example.mongo.core.controller;

import com.example.mongo.core.dao.UserDao;
import com.example.mongo.core.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class IndexController {

    @Autowired
    private UserDao userDao;

    @RequestMapping("find")
    public Object find(String name) {
        return this.userDao.findByName(name);
    }


    @RequestMapping("save")
    public Object save(User user) {
        return this.userDao.save(user);
    }

}
