package com.didi.chameleon.example;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.adapter.ICmlDegradeAdapter;

import java.util.HashMap;

public class CmlDegradeDefault implements ICmlDegradeAdapter {

    @Override
    public DegradeViewWrapper getDegradeView(int degradeCode) {
        return null;
    }

    @Override
    public void degradeActivity(@NonNull Activity activity, @NonNull String url, @Nullable HashMap<String, Object> options, int degradeCode) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        CmlEngine.getInstance().launchPage(activity, url, null);
    }
}
