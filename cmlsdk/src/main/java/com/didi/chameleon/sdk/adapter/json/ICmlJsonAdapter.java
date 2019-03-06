package com.didi.chameleon.sdk.adapter.json;

public interface ICmlJsonAdapter {

    <T> String toJson(T object);

    <T> T fromJson(String json, Class<T> classOfT);

}
