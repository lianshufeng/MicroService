package com.github.microservice.auth.client.model.stream;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.microservice.auth.client.type.AuthEventType;
import lombok.Getter;
import lombok.NoArgsConstructor;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EnterpriseStreamModel.class, name = "EnterpriseStreamModel"),
        @JsonSubTypes.Type(value = EnterpriseUserStreamModel.class, name = "EnterpriseUserStreamModel"),
        @JsonSubTypes.Type(value = RoleStreamModel.class, name = "RoleStreamModel"),
        @JsonSubTypes.Type(value = TokenStreamModel.class, name = "TokenStreamModel"),
        @JsonSubTypes.Type(value = RoleGroupUserStreamModel.class, name = "RoleGroupUserStreamModel"),
        @JsonSubTypes.Type(value = UserStreamModel.class, name = "UserStreamModel"),
        @JsonSubTypes.Type(value = RoleGroupStreamModel.class, name = "RoleGroupStreamModel")
})
@NoArgsConstructor
public abstract class SuperStreamModel {

    /**
     * 获取流的类型
     *
     * @return
     */
    public String type() {
        return this.getClass().getSimpleName();
    }


    /**
     * 权限类型
     */
    @Getter
    private AuthEventType eventType;

    /**
     * 数据id
     */
    @Getter
    private String id;


    /**
     * 参数
     */
    @Getter
    private Object[] items;


    public SuperStreamModel(AuthEventType eventType, String id) {
        this.eventType = eventType;
        this.id = id;
    }

    public SuperStreamModel(AuthEventType eventType, String id, Object[] items) {
        this.eventType = eventType;
        this.id = id;
        this.items = items;
    }
}

