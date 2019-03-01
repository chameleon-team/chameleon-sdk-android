package com.didi.chameleon.weex.container;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.adapter.ICmlDegradeAdapter;
import com.didi.chameleon.sdk.container.ICmlView;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.weex.CmlWeexViewInstance;

import java.util.HashMap;

/**
 * 用来展示 Chameleon Weex view，需要调用相应的生命周期。渲染通过调用 {@link #render(String, HashMap)}
 *
 * @since 18/5/24
 */
public class CmlView extends FrameLayout implements CmlWeexViewInstance.ICmlInstanceListener, ICmlView {
    private CmlWeexViewInstance wxInstance;
    private ICmlDegradeAdapter.DegradeViewWrapper degradeViewWrapper;
    private boolean isDestroy;
    private HashMap<String, Object> options;

    public CmlView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CmlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CmlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        wxInstance = new CmlWeexViewInstance(CmlView.this, CmlView.this);
    }

    /**
     * @param url     weex Bundle地址
     * @param options 额外需要传递的参数
     */
    @Override
    public void render(String url, HashMap<String, Object> options) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        this.options = options;
        wxInstance.render(url, options);
    }

    @Override
    public void invokeJsMethod(String module, String method, String args, CmlCallback callback) {
        if (wxInstance != null) {
            wxInstance.invokeJsMethod(module, method, args, callback);
        }
    }

    @Override
    public void onCreate() {
        if (wxInstance != null) {
            wxInstance.onCreate();
        }
        isDestroy = false;
    }

    @Override
    public void onResume() {
        if (wxInstance != null) {
            wxInstance.onResume();
        }
    }

    @Override
    public void onPause() {
        if (wxInstance != null) {
            wxInstance.onPause();
        }
    }

    @Override
    public void onStop() {
        if (wxInstance != null) {
            wxInstance.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (wxInstance != null) {
            wxInstance.onDestroy();
        }
        if (degradeViewWrapper != null) {
            degradeViewWrapper.onDestroy();
        }
        isDestroy = true;
    }

    @Override
    public void onDegradeToH5(String url, int degradeCode) {
        if (getContext() == null || isDestroy) {
            return;
        }

        ICmlDegradeAdapter degradeAdapter = CmlEnvironment.getDegradeAdapter();
        if (degradeAdapter == null) {
            return;
        }

        if (degradeViewWrapper == null) {
            degradeViewWrapper = degradeAdapter.getDegradeView(degradeCode);
            removeAllViews();
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(degradeViewWrapper.getView(getContext()), layoutParams);
        }
        degradeViewWrapper.loadURL(getContext(), url, options);
    }

    @Override
    public void onViewCreate(View view) {
        this.addView(view);
    }

    @Override
    public void onRenderSuccess() {

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
    public boolean isInDialog() {
        return false;
    }

    @Override
    public boolean isValid() {
        return !isDestroy;
    }

    @Override
    public void finishSelf() {
        // NOTHING
    }

    public CmlWeexViewInstance getInstance() {
        return wxInstance;
    }
}
