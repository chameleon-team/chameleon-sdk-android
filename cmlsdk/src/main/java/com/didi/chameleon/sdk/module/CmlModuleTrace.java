package com.didi.chameleon.sdk.module;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlActivityInstance;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.ICmlViewInstance;

import java.util.concurrent.atomic.AtomicLong;

public class CmlModuleTrace {

    private static AtomicLong ADMIN_CALLBACK_ID = new AtomicLong(0);

    public String getUniqueCallbackId(String instanceId) {
        return String.valueOf(ADMIN_CALLBACK_ID.incrementAndGet());
    }

    public Context getContext(@Nullable String instanceId) {
        if (instanceId == null) {
            return CmlEngine.getInstance().getAppContext();
        } else {
            return CmlInstanceManage.getInstance().getCmlInstance(instanceId).getContext();
        }
    }

    public Activity getActivity(@NonNull String instanceId) {
        Context context = getContext(instanceId);
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }

    public ICmlActivityInstance getActivityInstance(@NonNull String instanceId) {
        return CmlInstanceManage.getInstance().getCmlActivityInstance(instanceId);
    }

    public ICmlViewInstance getViewInstance(@NonNull String instanceId) {
        return CmlInstanceManage.getInstance().getCmlViewInstance(instanceId);
    }

    public ICmlInstance getInstance(@NonNull String instanceId) {
        return CmlInstanceManage.getInstance().getCmlInstance(instanceId);
    }

}
