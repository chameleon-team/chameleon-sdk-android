package com.didi.chameleon.web;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlViewInstance;
import com.didi.chameleon.sdk.container.ICmlView;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlModuleManager;
import com.didi.chameleon.sdk.utils.Util;
import com.didi.chameleon.web.bridge.BaseWebView;

import java.util.HashMap;

public class CmlWebViewInstance implements ICmlViewInstance {
    private static final String TAG = "CmlWebViewInstance";

    private BaseWebView mWebView;
    private ICmlView mCmlView;

    private String mUrl;
    private String mTotalUrl;

    private String mInstanceId;

    private HashMap<String, Object> extendsParam;

    public CmlWebViewInstance(@NonNull ICmlView cmlView,
                              @NonNull String instanceId,
                              BaseWebView webView) {
        mCmlView = cmlView;
        mInstanceId = instanceId;
        mWebView = webView;
    }

    @Override
    public void onCreate() {
        CmlInstanceManage.getInstance().addInstance(this);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        CmlInstanceManage.getInstance().removeInstance(mInstanceId);
    }

    @Override
    public String getInstanceId() {
        return mInstanceId;
    }

    @Nullable
    @Override
    public String getTargetURL() {
        return mTotalUrl;
    }

    @Nullable
    @Override
    public String getCurrentURL() {
        return mUrl;
    }

    @Override
    public void nativeToJs(String protocol) {
        if (mWebView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.evaluateJavascript(protocol, null);
            } else {
                mWebView.loadUrl(protocol);
            }
        }
    }

    @Override
    public void reload(String url) {

    }

    @Override
    public void degradeToH5(int degradeCode) {

    }

    @Override
    public void onResult(int resultCode, String result) {

    }

    @Override
    public void render(final String url, HashMap<String, Object> options) {
        mTotalUrl = url;
        mUrl = Util.parseH5Url(url);
        this.extendsParam = extendsParam;
        final StringBuilder loadUrl = new StringBuilder(mTotalUrl);
        if (loadUrl.indexOf("?") < 0) {
            loadUrl.append("?");
        } else {
            loadUrl.append("&");
        }
        loadUrl.append(CmlEnvironment.CML_QUERY_SDK).append("=").append(CmlEnvironment.VERSION);
        if (extendsParam != null) {
            for (String key : extendsParam.keySet()) {
                loadUrl.append("&").append(key).append("=").append(extendsParam.get(key));
            }
        }
        if (null != mWebView) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(loadUrl.toString());
                }
            }, 100);
        }
    }

    @Override
    public void invokeJsMethod(String module, String method, String args, CmlCallback callback) {
        CmlModuleManager.getInstance().invokeWeb(mInstanceId, module, method, args, callback);
    }

    @Override
    public Context getContext() {
        return mCmlView.getContext();
    }

    @Nullable
    @Override
    public View getObjectView() {
        return mCmlView.getObjectView();
    }

    @Override
    public boolean isActivity() {
        return mCmlView.isActivity();
    }

    @Override
    public boolean isView() {
        return mCmlView.isView();
    }

    @Override
    public boolean isInDialog() {
        return mCmlView.isInDialog();
    }

    @Override
    public boolean isValid() {
        return mCmlView.isValid();
    }

    @Override
    public void finishSelf() {
        // do nothing
    }
}
