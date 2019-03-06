package com.didi.chameleon.sdk.common;

import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.adapter.CmlAdapterException;
import com.didi.chameleon.sdk.adapter.json.CmlFastJsonDefault;
import com.didi.chameleon.sdk.adapter.json.CmlGsonDefault;
import com.didi.chameleon.sdk.adapter.json.ICmlJsonAdapter;

public class CmlJsonWrapper implements ICmlJsonAdapter {

    private ICmlJsonAdapter adapter;

    public CmlJsonWrapper(@Nullable ICmlJsonAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public <T> String toJson(T object) {
        tryCreateAdapter();
        return adapter.toJson(object);
    }

    @Override
    public <T> T fromJson(String json, Class<T> classOfT) {
        tryCreateAdapter();
        return adapter.fromJson(json, classOfT);
    }

    private void tryCreateAdapter() {
        if (adapter != null) {
            return;
        }
        try {
            this.adapter = CmlGsonDefault.getDefault();
            return;
        } catch (ClassNotFoundException ignored) {

        }
        try {
            this.adapter = CmlFastJsonDefault.getDefault();
            return;
        } catch (ClassNotFoundException ignored) {

        }
        throw CmlAdapterException.throwAdapterNone(ICmlJsonAdapter.class);
    }
}
