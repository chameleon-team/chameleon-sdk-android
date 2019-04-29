package com.didi.chameleon.rn;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.didi.chameleon.rn.container.CmlRnActivity;
import com.didi.chameleon.sdk.CmlConstant;
import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.ICmlEngine;
import com.didi.chameleon.sdk.ICmlLaunchCallback;
import com.didi.chameleon.sdk.bundle.CmlBundle;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.sdk.utils.Util;
import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleEngine;
import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleEnvironment;
import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleManager;
import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleMgrConfig;
import com.didi.chameleon.weex.jsbundlemgr.code.CmlGetCodeStringCallback;

import java.util.HashMap;
import java.util.List;

public class CmlRnEngine implements ICmlEngine {
    private static final String TAG = "CmlRnEngine";

    private CmlJsBundleManager cmlJsBundleManager;

    static CmlRnEngine getInstance() {
        return (CmlRnEngine) CmlEngine.getInstance().getCmlEngine();
    }

    @Override
    public void init(Context context) {
        CmlLogUtil.d(TAG, "engine init.");
        initJsBundleManager(context);
    }

    @Override
    public void launchPage(@NonNull Activity activity, String url, HashMap<String, Object> options) {
        if (TextUtils.isEmpty(Util.parseCmlUrl(url))) {
            CmlLogUtil.e(TAG, "launchPage failed, url is: " + url);
            if (CmlEnvironment.getDegradeAdapter() != null) {
                CmlEnvironment.getDegradeAdapter().degradeActivity(activity, url, options, CmlConstant.FAILED_TYPE_DEGRADE);
            }
            return;
        }
        new CmlRnActivity.Launch(activity, url).addOptions(options).launch();
    }

    @Override
    public void launchPage(@NonNull Activity activity, String url, HashMap<String, Object> options, int requestCode, ICmlLaunchCallback launchCallback) {
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("file://")) {
            CmlLogUtil.e(TAG, "launchPage failed, url is: " + url);
            return;
        }

        new CmlRnActivity.Launch(activity, url)
                .addOptions(options)
                .addRequestCode(requestCode)
                .addLaunchCallback(launchCallback)
                .launchForResult();
    }

    private void initJsBundleManager(Context context) {
        cmlJsBundleManager = CmlJsBundleEngine.getInstance();
        CmlJsBundleEnvironment.CML_ALLOW_CACHE = CmlEnvironment.CML_ALLOW_BUNDLE_CACHE;
        CmlJsBundleEnvironment.DEBUG = CmlEnvironment.CML_DEBUG;
        cmlJsBundleManager.initConfig(context, new CmlJsBundleMgrConfig.Builder()
                .setMaxPreloadSize(CmlEnvironment.getMaxPreloadSize())
                .setMaxRuntimeSize(CmlEnvironment.getMaxRuntimeSize())
                .build());
    }

    @Override
    public void initPreloadList(List<CmlBundle> preloadList) {
        if (null == cmlJsBundleManager) {
            CmlLogUtil.e(TAG, "performPreload failed, CmlJsBundleManager is null.");
            return;
        }
        cmlJsBundleManager.setPreloadList(preloadList);
    }

    /**
     * 开始预加载
     */
    @Override
    public void performPreload() {
        if (null == cmlJsBundleManager) {
            CmlLogUtil.e(TAG, "performPreload failed, CmlJsBundleManager is null.");
            return;
        }
        cmlJsBundleManager.startPreload();
    }

    /**
     * 开始获取代码
     */
    void performGetCode(String url, CmlGetCodeStringCallback callback) {
        if (null == cmlJsBundleManager) {
            CmlLogUtil.e(TAG, "performPreload failed, CmlJsBundleManager is null.");
            return;
        }
        cmlJsBundleManager.getTemplate(url, callback);
    }
}
