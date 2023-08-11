package com.github.microservice.components.data.base.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleDataFilter {

    private String key;

    private Type type = Type.Is;

    private Object value;


    public enum Type {
        Is,
        Like,
        ;
    }


}
