package com.didi.chameleon.weex;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.didi.chameleon.sdk.CmlConstant;
import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.ICmlEngine;
import com.didi.chameleon.sdk.ICmlLaunchCallback;
import com.didi.chameleon.sdk.bridge.ICmlBridge;
import com.didi.chameleon.sdk.bridge.ICmlBridgeProtocol;
import com.didi.chameleon.sdk.bundle.CmlBundle;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.sdk.utils.Util;
import com.didi.chameleon.weex.adapter.CmlDefaultJsExceptionAdapter;
import com.didi.chameleon.weex.adapter.ICmlJSExceptionAdapter;
import com.didi.chameleon.weex.adapter.WXImgLoaderAdapter;
import com.didi.chameleon.weex.adapter.WxJsExceptionAdapter;
import com.didi.chameleon.weex.bridge.CmlWeexBridge;
import com.didi.chameleon.weex.bridge.CmlWeexBridgeJsToNative;
import com.didi.chameleon.weex.component.CmlWeexRichText;
import com.didi.chameleon.weex.container.CmlWeexActivity;
import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleEngine;
import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleEnvironment;
import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleManager;
import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleMgrConfig;
import com.didi.chameleon.weex.jsbundlemgr.code.CmlGetCodeStringCallback;
import com.didi.chameleon.weex.richtextcomponent.CmlRichTextComponent;
import com.taobao.gcanvas.bridges.weex.GCanvasWeexModule;
import com.taobao.gcanvas.bridges.weex.WXGCanvasWeexComponent;
import org.apache.weex.InitConfig;
import org.apache.weex.WXSDKEngine;
import org.apache.weex.common.WXException;

import java.util.HashMap;
import java.util.List;

/**
 * <h3>Weex 引擎初始化入口</h3>
 * <p>
 * 此类是 <em>Weex Engine</em> 包装入口类，提供基本的初始化入口和 包装了 Weex SDK 容器的调起能力。
 * Weex 特有的一些能力需要初始化的，也会在这里进行初始化。
 * </p>
 */

public class CmlWeexEngine implements ICmlEngine {
    private static final String TAG = "CmlWeexEngine";
    private ICmlJSExceptionAdapter cmlJSExceptionAdapter = new CmlDefaultJsExceptionAdapter();
    private CmlJsBundleManager cmlJsBundleManager;

    public static CmlWeexEngine getInstance() {
        return (CmlWeexEngine) CmlEngine.getInstance().getCmlEngine();
    }

    @Override
    public void init(Context context) {
        initWeex(context);
        ICmlBridge cmlBridge = new CmlWeexBridge();
        cmlBridge.init();
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
        new CmlWeexActivity.Launch(activity, url).addOptions(options).launch();
    }

    @Override
    public void launchPage(@NonNull Activity activity, String url, HashMap<String, Object> options, int requestCode, ICmlLaunchCallback launchCallback) {
        if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("file://")) {
            CmlLogUtil.e(TAG, "launchPage failed, url is: " + url);
            return;
        }

        new CmlWeexActivity.Launch(activity, url)
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

    private void initWeex(Context context) {
        CmlLogUtil.d(TAG, "init cml weex engine");

        InitConfig.Builder builder = new InitConfig.Builder();
        builder.setImgAdapter(new WXImgLoaderAdapter(CmlEnvironment.getImgLoaderAdapter()));
        builder.setJSExceptionAdapter(new WxJsExceptionAdapter(this.cmlJSExceptionAdapter));
        WXSDKEngine.initialize((Application) context.getApplicationContext(), builder.build());
        try {
            // bridge
            WXSDKEngine.registerModule(ICmlBridgeProtocol.CML_BRIDGE, CmlWeexBridgeJsToNative.class, false);
            // rich text component
            WXSDKEngine.registerComponent(CmlRichTextComponent.NAME, CmlWeexRichText.class);
            // gcanvas component
            WXSDKEngine.registerModule("gcanvas", GCanvasWeexModule.class);
            WXSDKEngine.registerComponent("gcanvas", WXGCanvasWeexComponent.class);
        } catch (WXException e) {
            CmlLogUtil.d(TAG, "register weex bridge module error.");
        }
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
