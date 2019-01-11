package com.didi.chameleon.sdk.module;

import android.support.annotation.NonNull;

public class CmlCallbackSimple extends CmlCallbackAction<CmlCallbackSimple.Status> {

    public enum Status {
        SUCCESS, FAIL
    }

    public CmlCallbackSimple(String instanceId, String callbackId) {
        super(Status.class, instanceId, callbackId);
    }

    public void onSuccess() {
        onCallback(Status.SUCCESS);
    }

    public void onFail() {
        onError(ERROR_DEFAULT, null, Status.FAIL);
    }

    @Override
    protected void callbackWeb(@NonNull CmlCallbackModel<Status> data) {
        data.data = null;
        super.callbackWeb(data);
    }
}
