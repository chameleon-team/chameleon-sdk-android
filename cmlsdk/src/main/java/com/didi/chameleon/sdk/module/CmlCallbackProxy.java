package com.didi.chameleon.sdk.module;

import android.support.annotation.Nullable;

public final class CmlCallbackProxy<T> extends CmlCallback<T> {

    public String instanceId;
    public String callbackId;
    private CmlCallback<T> callback;

    public CmlCallbackProxy(String instanceId, String callbackId, CmlCallback<T> callback) {
        super(callback.dataClass);
        this.instanceId = instanceId;
        this.callbackId = callbackId;
        this.callback = callback;
    }

    @Override
    public void onCallback(@Nullable T data) {
        callback.onCallback(data);
    }
}
