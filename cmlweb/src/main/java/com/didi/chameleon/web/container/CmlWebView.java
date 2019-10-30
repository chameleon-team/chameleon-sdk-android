package com.didi.chameleon.web.container;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.container.CmlContainerView;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.web.CmlWebViewInstance;
import com.didi.chameleon.web.bridge.BaseWebView;

import java.util.HashMap;

public class CmlWebView extends CmlContainerView {
    private CmlWebViewInstance mWebInstance;
    private BaseWebView mBaseWebView;
    private boolean isDestroy;

    public CmlWebView(@NonNull Context context) {
        super(context);
    }

    public CmlWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CmlWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void render(String url, HashMap<String, Object> options) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        mWebInstance.render(url, null);
    }

    @Override
    public void invokeJsMethod(String module, String method, String args, CmlCallback callback) {
        if (mWebInstance != null) {
            mWebInstance.invokeJsMethod(module, method, args, callback);
        }
    }

    @Override
    public void onCreate() {
        mBaseWebView = new BaseWebView(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mBaseWebView, layoutParams);

        String instanceId = CmlEngine.getInstance().generateInstanceId();
        mWebInstance = new CmlWebViewInstance(CmlWebView.this, instanceId, mBaseWebView);
        mWebInstance.onCreate();
        mBaseWebView.setWebViewClient(mWebInstance);
        isDestroy = false;
    }

    @Override
    public void onResume() {
        if (mWebInstance != null) {
            mWebInstance.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mWebInstance != null) {
            mWebInstance.onPause();
        }
    }

    @Override
    public void onStop() {
        if (mWebInstance != null) {
            mWebInstance.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (mWebInstance != null) {
            mWebInstance.onDestroy();
        }
        isDestroy = true;
    }

    @Override
    public boolean isValid() {
        return !isDestroy;
    }

    @Override
    public boolean onBackPressed() {
        if (mBaseWebView != null && mBaseWebView.canGoBack()) {
            return true;
        }
        return super.onBackPressed();
    }
}
