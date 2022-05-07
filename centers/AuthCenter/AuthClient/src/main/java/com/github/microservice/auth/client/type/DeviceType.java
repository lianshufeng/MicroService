package com.github.microservice.auth.client.type;

import lombok.Getter;

/**
 * 设备类型
 */
public enum DeviceType {
    Pc("PC"),
    App("App"),
    wxApp("微信小程序"),

    ;


    DeviceType(String remark) {
        this.remark = remark;
    }

    @Getter
    private String remark;


}
