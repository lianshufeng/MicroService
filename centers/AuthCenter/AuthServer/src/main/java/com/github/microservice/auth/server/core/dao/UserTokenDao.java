package com.github.microservice.auth.server.core.dao;

import com.github.microservice.auth.client.type.DeviceType;
import com.github.microservice.auth.server.core.dao.extend.UserTokenDaoExtend;
import com.github.microservice.auth.server.core.domain.User;
import com.github.microservice.auth.server.core.domain.UserToken;
import com.github.microservice.components.data.mongo.mongo.dao.MongoDao;

import java.util.List;

public interface UserTokenDao extends MongoDao<UserToken>, UserTokenDaoExtend {
    /**
     * 查询刷新令牌是否存在
     *
     * @param refreshToken
     * @return
     */
    boolean existsByRefreshToken(String refreshToken);


    /**
     * 通过刷新令牌删除记录
     *
     * @param refreshToken
     * @return
     */
    long removeByRefreshToken(String refreshToken);


    /**
     * 查询客户端与用户
     *
     * @param clientId
     * @param user
     * @return
     */
    List<UserToken> findByClientIdAndDeviceTypeAndUser(String clientId, DeviceType deviceType, User user);

    /**
     * 通过刷新令牌查询到该令牌
     *
     * @param refreshToken
     * @return
     */
    UserToken findByRefreshToken(String refreshToken);
}
