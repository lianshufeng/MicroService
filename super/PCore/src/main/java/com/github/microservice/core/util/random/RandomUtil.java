package com.github.microservice.core.util.random;

import java.util.Random;

public class RandomUtil {


    /**
     * 取指定返回的随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int nextInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }


}
