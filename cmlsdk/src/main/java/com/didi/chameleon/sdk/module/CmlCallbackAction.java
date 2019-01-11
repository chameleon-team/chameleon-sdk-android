package com.didi.chameleon.sdk.module;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.utils.CmlLogUtil;

public class CmlCallbackAction<T> extends CmlCallback<T> {


    private String instanceId;
    private String callbackId;

    public CmlCallbackAction(Class<T> dataClass, String instanceId, String callbackId) {
        super(dataClass);
        this.instanceId = instanceId;
        this.callbackId = callbackId;
    }

    @Override
    public void onCallback(@Nullable T data) {
        CmlCallbackModel<T> model = new CmlCallbackModel<>();
        model.errorNo = 0;
        model.msg = "";
        model.data = data;
        callbackWeb(model);
    }

    @Override
    public void onError(@IntRange(from = 1) int errorNo, String msg, T data) {
        CmlCallbackModel<T> model = new CmlCallbackModel<>();
        model.errorNo = errorNo;
        model.msg = msg;
        model.data = data;
        callbackWeb(model);
    }

    protected void callbackWeb(@NonNull CmlCallbackModel<T> data) {
        if (callbackId == null) {
            CmlLogUtil.i("cml", "callback id is empty");
            return;
        }
        CmlModuleManager.getInstance().callbackWeb(instanceId, callbackId, data);
    }
}
