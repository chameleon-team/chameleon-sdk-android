package com.didi.chameleon.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.didi.chameleon.sdk.CmlBaseLifecycle;
import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlActivityInstance;
import com.didi.chameleon.sdk.container.ICmlActivity;
import com.didi.chameleon.sdk.utils.Util;
import com.didi.chameleon.web.bridge.CmlWebView;

import java.util.HashMap;

public class CmlWebInstance implements ICmlActivityInstance, CmlBaseLifecycle {

    private static final String TAG = "CmlWebInstance";

    private String mInstanceId;
    private CmlWebView mWebView;

    private String mUrl;
    private String mTotalUrl;

    private Activity mActivity;
    private ICmlActivity mCmlContainer;

    private HashMap<String, Object> extendsParam;

    public String getInstanceId() {
        return mInstanceId;
    }

    public CmlWebInstance(@NonNull Activity activity,
                          @NonNull ICmlActivity cmlContainer) {
        mActivity = activity;
        mCmlContainer = cmlContainer;
    }

    public void setWebView(CmlWebView webView) {
        mWebView = webView;
    }

    /**
     * 初始化param状态，当reload页面时不需要再次初始化param
     */
    private boolean initParamSuccess = false;

    @Override
    public void onCreate() {
        mInstanceId = CmlEngine.getInstance().generateInstanceId();
        // 注册到框架里
        CmlInstanceManage.getInstance().addActivityInstance(mActivity, mInstanceId, this);

        if (!initParamSuccess) {
            initParam();
            initParamSuccess = true;
        }
    }

    private void initParam() {
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
        CmlInstanceManage.getInstance().removeActivityInstance(mInstanceId);
    }

    public void onActivityDestroy() {

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
        StringBuilder loadUrl = new StringBuilder(mTotalUrl);
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
            mWebView.loadUrl(loadUrl.toString());
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
        return mActivity;
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
