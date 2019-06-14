package com.didi.chameleon.weex.container;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.adapter.ICmlDegradeAdapter;
import com.didi.chameleon.sdk.container.CmlContainerView;
import com.didi.chameleon.sdk.container.ICmlView;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.weex.CmlWeexViewInstance;

import java.util.HashMap;

/**
 * 用来展示 Chameleon Weex view，需要调用相应的生命周期。渲染通过调用 {@link #render(String, HashMap)}
 *
 * @since 18/5/24
 */
public class CmlWeexView extends CmlContainerView implements CmlWeexViewInstance.ICmlInstanceListener {
    private CmlWeexViewInstance wxInstance;
    private ICmlDegradeAdapter.DegradeViewWrapper degradeViewWrapper;
    private boolean isDestroy;
    private HashMap<String, Object> options;
    private IDegradeToH5 degradeToH5;

    public CmlWeexView(@NonNull Context context) {
        super(context);
    }

    public CmlWeexView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CmlWeexView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        wxInstance = new CmlWeexViewInstance(CmlWeexView.this, CmlWeexView.this);
    }

    public void setDegradeToH5(IDegradeToH5 degradeToH5) {
        this.degradeToH5 = degradeToH5;
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
            View view = degradeViewWrapper.getView(getContext());
            addView(view, layoutParams);
            if (null != degradeToH5 && view instanceof ICmlView) {
                degradeToH5.setView((ICmlView) view);
            }
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

    @Override
    public boolean isValid() {
        return !isDestroy;
    }

    public CmlWeexViewInstance getInstance() {
        return wxInstance;
    }

    public interface IDegradeToH5 {
        void setView(ICmlView cmlView);
    }
}
