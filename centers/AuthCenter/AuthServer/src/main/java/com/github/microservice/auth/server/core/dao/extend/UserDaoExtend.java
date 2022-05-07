package com.github.microservice.auth.server.core.dao.extend;

import com.github.microservice.auth.client.model.UserLoginModel;
import com.github.microservice.auth.client.type.LoginType;

public interface UserDaoExtend {

    /**
     * 修改登陆名
     *
     * @return
     */
    boolean updateLoginName(UserLoginModel userLogin);


    /**
     * 禁用/启用用户
     *
     * @param uid
     * @param disable
     * @return
     */
    boolean disable(String uid, boolean disable);


    /**
     * 更新用户密码
     * @param uid
     * @param passWord
     * @return
     */
    boolean updatePassword(String uid, String passWord);


}
