package com.didi.chameleon.web.container;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.container.ICmlView;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.web.CmlWebViewInstance;
import com.didi.chameleon.web.bridge.BaseWebView;

import java.util.HashMap;

public class CmlWebView extends FrameLayout implements ICmlView {
    private CmlWebViewInstance mWebInstance;
    private BaseWebView mBaseWebView;
    private boolean isDestroy;

    public CmlWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CmlWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CmlWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
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
        addView(mBaseWebView);

        String instanceId = CmlEngine.getInstance().generateInstanceId();
        mWebInstance = new CmlWebViewInstance(CmlWebView.this, instanceId, mBaseWebView);
        mBaseWebView.startApplication(mWebInstance);
        mWebInstance.onCreate();
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

    @Nullable
    @Override
    public View getObjectView() {
        return this;
    }

    @Override
    public boolean isActivity() {
        return false;
    }

    @Override
    public boolean isView() {
        return true;
    }

    @Override
    public void finishSelf() {

    }

    @Override
    public boolean isInDialog() {
        return false;
    }

    @Override
    public boolean isValid() {
        return !isDestroy;
    }
}
