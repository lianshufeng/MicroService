package com.github.microservice.components.data.jpa.helper;

import org.springframework.stereotype.Component;

@Component
public class DBHelper {

    /**
     * 取时间
     *
     * @return
     */
    public long getTime() {
        return  System.currentTimeMillis();
    }
}
