package com.github.microservice.auth.client.type;

import lombok.Getter;

public enum ResourceType {

    AuthName("权限名"),
    IdentityName("身份名"),
    ;

    ResourceType(String remark) {
        this.remark = remark;
    }

    //备注
    @Getter
    private String remark;


}
