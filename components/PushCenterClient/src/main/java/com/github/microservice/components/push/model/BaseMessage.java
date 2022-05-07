package com.github.microservice.components.push.model;

import com.github.microservice.components.push.type.MessageType;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public abstract class BaseMessage implements Serializable {


    /**
     * 内容数据
     */
    private Map<String, Object> content;



}
