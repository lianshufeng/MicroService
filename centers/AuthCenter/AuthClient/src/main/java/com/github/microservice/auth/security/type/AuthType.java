package com.github.microservice.auth.security.type;

import lombok.Getter;

public enum AuthType {

    //增加是否单例, 仅用户是特殊的
    User("ur", true, false, "用户"),
    Platform("pf", true, true, "平台"),
    Enterprise("ep", false, true, "企业"),
    Project("pj", false, true, "项目"),


    ;


    //编码
    @Getter
    private String code;


    //是否单例
    @Getter
    private boolean single;


    //是否机构
    @Getter
    private boolean organization;

    //备注
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


    AuthType(String code, boolean single, boolean organization, String remark) {
        this.code = code;
        this.single = single;
        this.organization = organization;
        this.remark = remark;
    }
}
