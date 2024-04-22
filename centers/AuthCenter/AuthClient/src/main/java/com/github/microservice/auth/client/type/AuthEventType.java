package com.github.microservice.auth.client.type;

import lombok.Getter;

public enum AuthEventType {

    Add("添加"),

    Modify("修改"),

    Disable("禁用"),

    Enable("启用"),

    Remove("删除"),

    UnRegister("注销"),

    ;

    @Getter
    private String remark;

    AuthEventType(String remark) {
        this.remark = remark;
    }
}
