package com.example.mongo.core.service;

import com.example.mongo.core.dao.UserDao;
import com.example.mongo.core.domain.User;
import com.example.mongo.core.mapper.UserMapper;
import com.example.mongo.core.mapper.UserMapper2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMapper2 userMapper2;

    @RequestMapping("find")
    public Object find(String name) {
        return this.userDao.findByName(name);
    }


    @RequestMapping("findByName")
    public Object findByName(String name) {
        return this.userMapper2.findByName(name);
    }


    @Transactional
    @RequestMapping("put")
    public Object save(User user) {
        User u = this.userDao.save(user);
        System.out.println(u.getId());
        return u;
    }

    @Transactional
    @RequestMapping("delete")
    public Object delete(QueryUser user) {
        return this.userDao.deleteUser(user.getName());
    }


    @RequestMapping("list")
    public Object list() {
        return this.userMapper.getAllUsers();
    }


    @RequestMapping("listName")
    public Object listName(QueryUser user, Pageable pageable) {
        return this.userDao.listName(user.getName(), pageable);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QueryUser {
        //用户名
        private String name;
    }

}
