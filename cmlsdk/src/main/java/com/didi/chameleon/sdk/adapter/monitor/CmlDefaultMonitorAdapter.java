package com.didi.chameleon.sdk.adapter.monitor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

import java.util.Map;

public class CmlDefaultMonitorAdapter implements ICmlMonitorAdapter {

    @Override
    public void onError(@Nullable ICmlInstance instance, @NonNull ErrorInfo error) {
        if (!CmlEnvironment.CML_DEBUG) {
            return;
        }
        try {
            String msg = error.getMessage();
            String instanceId = error.getInstanceId();
            if (!TextUtils.isEmpty(instanceId)) {
                Context context = CmlInstanceManage.getInstance().getCmlInstance(instanceId).getContext();
                CmlEnvironment.getModalTip().showAlert(context, msg, "", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTrace(@NonNull ICmlInstance instance, @NonNull String event, @Nullable Map<String, Object> info) {
        StringBuilder builder = new StringBuilder(event);
        if (info != null) {
            builder.append(":");
            for (Map.Entry<String, Object> item : info.entrySet()) {
                builder.append(item.getKey()).append("-").append(item.getValue()).append(", ");
            }
        }
        CmlLogUtil.i("CmlTrace", builder.toString());
    }


}
