package com.github.microservice.auth.client.type;

import com.github.microservice.auth.client.event.auth.*;
import com.github.microservice.auth.client.model.stream.*;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public enum AuthStreamType {

    //用户
    User(UserStreamModel.class, UserApplicationEvent.class),

    //角色
    Role(RoleStreamModel.class, RoleApplicationEvent.class),

    //角色组
    RoleGroup(RoleGroupStreamModel.class, RoleGroupApplicationEvent.class),

    //角色组中的用户
    RoleGroupUser(RoleGroupUserStreamModel.class, RoleGroupUserApplicationEvent.class),

    //企业
    Enterprise(EnterpriseStreamModel.class, EnterpriseApplicationEvent.class),


    //企业用户
    EnterpriseUser(EnterpriseUserStreamModel.class,EnterpriseUserApplicationEvent.class),


    //令牌
    Token(TokenStreamModel.class, TokenApplicationEvent.class),
    ;

    AuthStreamType(Class<? extends SuperStreamModel> streamClass, Class<? extends ApplicationEvent> eventClass) {
        this.streamClass = streamClass;
        this.eventClass = eventClass;
    }

    @Getter
    private Class<? extends SuperStreamModel> streamClass;

    @Getter
    private Class<? extends ApplicationEvent> eventClass;


    /**
     * 通过流的数据对象类型反差枚举类
     *
     * @param streamClass
     * @return
     */
    public static AuthStreamType findByStreamClass(Class<? extends SuperStreamModel> streamClass) {
        for (AuthStreamType streamType : AuthStreamType.values()) {
            if (streamClass == streamType.getStreamClass()) {
                return streamType;
            }
        }
        return null;
    }


}
