package com.github.microservice.auth.security.type;

import lombok.Getter;

public enum AuthType {
    User("ur", "用户"),
    Enterprise("ep", "企业"),
    Platform("pf", "平台"),


    ;

    /**
     * 编码
     */
    @Getter
    private String code;

    /**
     * 备注
     */
    @Getter
    private String remark;


    /**
     * 构建权限名
     *
     * @param name
     * @return
     */
    public String makeAuthName(String name) {
        return this.code + "_" + name;
    }


    AuthType(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }
}
