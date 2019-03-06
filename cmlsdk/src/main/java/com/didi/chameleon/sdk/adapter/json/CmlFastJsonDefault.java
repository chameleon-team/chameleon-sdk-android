package com.didi.chameleon.sdk.adapter.json;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;

public class CmlFastJsonDefault implements ICmlJsonAdapter {

    @NonNull
    public static ICmlJsonAdapter getDefault() throws ClassNotFoundException {
        Class.forName("com.alibaba.fastjson.JSON");
        return new CmlFastJsonDefault();
    }

    @Override
    public <T> String toJson(T object) {
        return JSON.toJSONString(object);
    }

    @Override
    public <T> T fromJson(String json, Class<T> classOfT) {
        return JSON.parseObject(json, classOfT);
    }

}
