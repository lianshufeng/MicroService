package com.example.mongo.core.dao;

import com.example.mongo.core.dao.extend.UserDaoExtend;
import com.example.mongo.core.domain.User;
import com.github.microservice.components.data.jpa.Dao.JpaDataDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaDataDao<User>, UserDaoExtend {

    User findByName(String name);


    @Modifying
    @Query("delete from User u where name = ?1 ")
    int deleteUser(String name);


    @Query("select u from User u where name like %?1% ")
    Page<User> listName(String name, Pageable pageable);
}
