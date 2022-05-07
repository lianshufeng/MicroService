package com.github.microservice.auth.server.core.dao.extend;

import com.github.microservice.auth.client.type.DeviceType;
import com.github.microservice.auth.server.core.domain.UserToken;

import java.util.List;

public interface UserTokenDaoExtend {

    /**
     * 查询用户令牌
     *
     * @param deviceType
     * @param uid
     * @return
     */
    List<UserToken> findUserToken(DeviceType deviceType, String uid);


    /**
     * 设置用户令牌的TTL时间
     *
     * @param refreshToken
     * @param time
     */
    boolean setTTL(String refreshToken, long time);

}
