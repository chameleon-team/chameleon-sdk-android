package com.didi.chameleon.web;

import android.content.Context;
import android.support.annotation.NonNull;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.ICmlEngine;
import com.didi.chameleon.sdk.bridge.ICmlBridge;
import com.didi.chameleon.sdk.bundle.CmlBundle;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.web.bridge.CmlWebBridge;
import com.didi.chameleon.web.container.CmlWebActivity;

import java.util.HashMap;
import java.util.List;

public class CmlWebEngine implements ICmlEngine {
    public static final String TAG = "CmlWebEngine";

    public static CmlWebEngine getInstance() {
        return (CmlWebEngine) CmlEngine.getInstance().getCmlWebEngine();
    }

    @Override
    public void init(Context context) {
        ICmlBridge cmlBridge = new CmlWebBridge();
        cmlBridge.init();
    }

    @Override
    public void launchPage(@NonNull Context activity, String url, HashMap<String, Object> options) {
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("file://")) {
            CmlLogUtil.e(TAG, "launchPage failed, url is: " + url);
            return;
        }
        new CmlWebActivity.Launch(activity, url).addOptions(options).launch();
    }

    private void initJsBundleManager(Context context) {
        // do nothing
    }

    @Override
    public void initPreloadList(List<CmlBundle> preloadList) {
        // do nothing
    }

    @Override
    public void performPreload() {
        // do nothing
    }
}
