package com.didi.chameleon.sdk;

import android.content.Context;
import android.support.annotation.NonNull;

import com.didi.chameleon.sdk.bundle.CmlBundle;

import java.util.HashMap;
import java.util.List;

public interface ICmlEngine {
    void init(Context context);

    void initPreloadList(List<CmlBundle> preloadList);

    void performPreload();

    void launchPage(@NonNull Context activity, String url, HashMap<String, Object> options);
}
