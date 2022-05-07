package com.github.microservice.components.data.mongo.timer.core;

import lombok.experimental.Delegate;

import java.util.HashSet;
import java.util.Set;

public class SimpleTaskTimerContainer {


    private interface SimpleTaskTimerCoreDelegate {
        boolean add(SimpleTaskTimerCore item);
    }


    @Delegate(types = SimpleTaskTimerCoreDelegate.class)
    private Set<SimpleTaskTimerCore> simpleTaskTimerHelperSet = new HashSet<>();


}
