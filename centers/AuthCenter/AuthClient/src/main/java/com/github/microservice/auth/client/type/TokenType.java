package com.github.microservice.auth.client.type;

import lombok.Getter;

public enum TokenType {
    Access("访问令牌"),
    refresh("刷新令牌");

    @Getter
    private String remark;

    TokenType(String remark) {
        this.remark = remark;
    }
}
