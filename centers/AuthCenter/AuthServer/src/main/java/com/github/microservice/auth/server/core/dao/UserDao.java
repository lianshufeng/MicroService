package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.server.core.dao.extend.UserDaoExtend;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;

public interface UserDao extends MongoDao<User>, UserDaoExtend {

    /**
     * 用户名是否存在
     *
     * @param userName
     * @return
     */
    boolean existsByUserName(String userName);

    /**
     * 手机号码是否存在
     *
     * @param phone
     * @return
     */
    boolean existsByPhone(String phone);

    /**
     * 邮箱是否存在
     *
     * @param email
     * @return
     */
    boolean existsByEmail(String email);


    /**
     * @param phone
     * @return
     */
    User findByPhone(String phone);

    /**
     * @param userName
     * @return
     */
    User findByUserName(String userName);

    /**
     * @param idCard
     * @return
     */
    User findByIdcard(String idCard);

    /**
     * 通过邮箱查询
     *
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 通过用户id查询对象
     *
     * @param id
     * @return
     */
    User findTop1ById(String id);



}
