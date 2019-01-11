package com.didi.chameleon.sdk.adapter.json;

public interface CmlJsonAdapter {

    <T> String toJson(T object);

    <T> T fromJson(String json, Class<T> classOfT);

}
