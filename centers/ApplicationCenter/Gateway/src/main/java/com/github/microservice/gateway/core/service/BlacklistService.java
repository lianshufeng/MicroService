package com.github.microservice.gateway.core.service;

import com.github.microservice.gateway.core.dao.BlacklistDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlacklistService {


    @Autowired
    private BlacklistDao blacklistDao;

    /**
     * 是否存在黑名单中
     *
     * @return
     */
    public boolean exitsBlacklist(String ip, String roleName) {
        return this.blacklistDao.exitsBlacklist(ip, roleName);
    }


}
