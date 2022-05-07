package com.github.microservice.auth.server.core.dao.extend;

public interface TokenLoginDaoExtend {


    /**
     * 增加检验次数
     *
     * @param token
     * @return
     */
    boolean incCheckCount(String token);



}
