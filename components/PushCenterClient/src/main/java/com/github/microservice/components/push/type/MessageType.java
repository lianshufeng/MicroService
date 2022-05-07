package com.github.microservice.components.push.type;

import lombok.Getter;
import lombok.Setter;

public enum MessageType {

    Sms("短信"),
    Email("电子邮件"),
    App("应用"),
    Mq("mqtt"),
    ;

    @Getter
    @Setter
    private String remark;

    MessageType(String remark){
        this.remark = remark;
    }
}
