package com.didi.chameleon.sdk.container;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public abstract class CmlContainerView extends FrameLayout implements ICmlView {

    private Activity bindActivity;

    public CmlContainerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CmlContainerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CmlContainerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        if (context instanceof Activity) {
            this.bindActivity = (Activity) context;
        }
    }

    /**
     * @return 是否消耗物理返回键
     */
    public boolean onBackPressed() {
        return false;
    }

    public void bindActivity(Activity bindAty) {
        this.bindActivity = bindAty;
    }

    @Override
    public void finishSelf() {
        if (bindActivity != null) {
            bindActivity.finish();
        }
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

}
