package com.github.ms.demo.app.core.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {

    public long time() {
        return System.currentTimeMillis();
    }


}
