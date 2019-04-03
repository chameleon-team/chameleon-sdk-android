package com.didi.chameleon.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlActivityInstance;
import com.didi.chameleon.sdk.ICmlBaseLifecycle;
import com.didi.chameleon.sdk.ICmlLaunchCallback;
import com.didi.chameleon.sdk.container.ICmlActivity;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.sdk.utils.Util;
import com.didi.chameleon.web.bridge.BaseWebView;

import java.util.HashMap;

public class CmlWebInstance implements ICmlActivityInstance, ICmlBaseLifecycle {

    private static final String TAG = "CmlWebInstance";

    private String mInstanceId;
    private int mRequestCode;
    private BaseWebView mWebView;
    private ICmlActivity mCmlContainer;
    private HashMap<String, Object> extendsParam;
    private ICmlLaunchCallback mLaunchCallback;

    private String mUrl;
    private String mTotalUrl;

    public CmlWebInstance(@NonNull ICmlActivity cmlContainer,
                          @NonNull String instanceId,
                          int requestCode,
                          BaseWebView webView) {
        mCmlContainer = cmlContainer;
        mInstanceId = instanceId;
        mRequestCode = requestCode;
        mWebView = webView;
        mLaunchCallback = CmlInstanceManage.getInstance().getLaunchCallback(instanceId);

        CmlLogUtil.d(TAG, "instance id: " + instanceId);
        CmlLogUtil.d(TAG, "request code: " + requestCode);
    }

    @Override
    public void onCreate() {
        // 注册到框架里
        CmlInstanceManage.getInstance().addInstance(this);
        if (null != mLaunchCallback) {
            mLaunchCallback.onCreate();
        }
    }

    @Override
    public void onResume() {
        if (null != mLaunchCallback) {
            mLaunchCallback.onResume();
        }
    }

    @Override
    public void onPause() {
        if (null != mLaunchCallback) {
            mLaunchCallback.onPause();
        }
    }

    @Override
    public void onStop() {
        if (null != mLaunchCallback) {
            mLaunchCallback.onStop();
        }
    }

    @Override
    public void onDestroy() {
        CmlInstanceManage.getInstance().removeInstance(mInstanceId);

        if (null != mLaunchCallback) {
            mLaunchCallback.onDestroy();
        }
    }

    @Override
    public void onResult(int resultCode, String result) {
        if (null != mLaunchCallback) {
            mLaunchCallback.onResult(this, mRequestCode, resultCode, result);
        }
    }

    /**
     * 根据传入的 url 渲染页面
     *
     * @param url          传入的 url ,例如：http://xxx.xxx.xxx?cml_addr=xxx.xxx.xxx/xxx.js
     * @param extendsParam 需要传入给js 的参数
     */
    public void renderByUrl(@NonNull final String url, final HashMap<String, Object> extendsParam) {
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
    public void updateNaviTitle(String title) {
        if (null != mCmlContainer) {
            mCmlContainer.updateNaviTitle(title);
        }
    }

    /**
     * 重新加载当前页面
     */
    public void reload(String url) {
        if (!TextUtils.isEmpty(url)) {
            renderByUrl(url, extendsParam);
        } else if (!TextUtils.isEmpty(mTotalUrl)) {
            renderByUrl(mTotalUrl, extendsParam);
        }
    }

    @Override
    public void degradeToH5(int degradeCode) {
        // do nothing
    }

    /**
     * @return 获取当前 Instance 的 url
     */
    public String getTotalUrl() {
        return mTotalUrl;
    }

    @Override
    public Context getContext() {
        return mCmlContainer.getContext();
    }

    @Override
    public Activity getActivity() {
        return mCmlContainer.getActivity();
    }

    @Override
    public boolean isActivity() {
        return mCmlContainer.isActivity();
    }

    @Override
    public boolean isView() {
        return mCmlContainer.isView();
    }

    @Override
    public boolean isInDialog() {
        return mCmlContainer.isInDialog();
    }

    @Override
    public boolean isValid() {
        return mCmlContainer.isValid();
    }

    @Override
    public void finishSelf() {
        mCmlContainer.finishSelf();
    }

    @Override
    public void setPageResult(int resultCode, Intent data) {
        mCmlContainer.setPageResult(resultCode, data);
    }

    @Override
    public void overrideAnim(int enterAnim, int exitAnim) {
        mCmlContainer.overrideAnim(enterAnim, exitAnim);
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

    @Nullable
    @Override
    public View getObjectView() {
        return mCmlContainer.getObjectView();
    }

    @Override
    public void nativeToJs(@NonNull String protocol) {
        if (mWebView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.evaluateJavascript(protocol, null);
            } else {
                mWebView.loadUrl(protocol);
            }
        }
    }
}
