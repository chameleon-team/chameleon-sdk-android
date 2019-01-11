package com.didi.chameleon.sdk.module;

public class TestModel<T> {

    public T name;

    public TestModel(T name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "TestModel " + String.valueOf(name);
    }
}
