package com.didi.chameleon.sdk.module;

import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;

public abstract class CmlCallback<T> {

    public static final int ERROR_DEFAULT = 1;

    protected Class<T> dataClass;

    public CmlCallback(Class<T> dataClass) {
        this.dataClass = dataClass;
    }

    public abstract void onCallback(@Nullable T data);

    @CallSuper
    public final void onError(@IntRange(from = 1) int errorNo) {
        this.onError(errorNo, null);
    }

    @CallSuper
    public final void onError(@IntRange(from = 1) int errorNo, String msg) {
        this.onError(errorNo, msg, null);
    }

    public void onError(@IntRange(from = 1) int errorNo, String msg, T data) {

    }

    public boolean uiThread() {
        return true;
    }

}
