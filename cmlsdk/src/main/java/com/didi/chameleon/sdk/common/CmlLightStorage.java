package com.didi.chameleon.sdk.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.adapter.storage.CmlStorageDefault;
import com.didi.chameleon.sdk.adapter.storage.ICmlStorageAdapter;

public class CmlLightStorage implements ICmlStorageAdapter {

    private ICmlStorageAdapter adapter;

    public CmlLightStorage(@NonNull Context context, @Nullable ICmlStorageAdapter adapter) {
        this.adapter = adapter != null ? adapter : CmlStorageDefault.getDefault(context);
    }


    @Override
    public void save(String key, String data) {
        adapter.save(key, data);
    }

    @Override
    public String get(String key) {
        return adapter.get(key);
    }

    @Override
    public void remove(String key) {
        adapter.remove(key);
    }
}
