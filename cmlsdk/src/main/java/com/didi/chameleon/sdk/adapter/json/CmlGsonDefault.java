package com.didi.chameleon.sdk.adapter.json;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

public class CmlGsonDefault implements CmlJsonAdapter {

    @NonNull
    public static CmlJsonAdapter getDefault() throws ClassNotFoundException {
        Class.forName("com.google.gson.Gson");
        return new CmlGsonDefault();
    }

    private Gson gson;

    public CmlGsonDefault() {
        gson = new Gson();
    }

    @Override
    public <T> String toJson(T object) {
        return gson.toJson(object);
    }

    @Override
    public <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

}
