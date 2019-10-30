package com.didi.chameleon.sdk.adapter.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class CmlStorageDefault implements ICmlStorageAdapter {

    public static final String SP_NAME = "cml_sp";

    public static CmlStorageDefault getDefault(Context context) {
        return new CmlStorageDefault(context);
    }

    protected SharedPreferences sp;

    public CmlStorageDefault(Context context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void save(String key, String data) {
        sp.edit().putString(key, data).apply();
    }

    @Override
    public String get(String key) {
        return sp.getString(key, "");
    }

    @Override
    public void remove(String key) {
        sp.edit().remove(key).apply();
    }
}
