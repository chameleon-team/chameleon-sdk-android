package com.didi.chameleon.sdk.adapter.monitor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.ICmlInstance;

import java.util.Map;

public interface ICmlMonitorAdapter {

    String TRACE_PERFORMANCE = "performance";

    interface ErrorInfo {

        String getMessage();

        String getInstanceId();

        Object getError();
    }

    /**
     * 异常发生
     */
    void onError(@Nullable ICmlInstance instance, @NonNull ErrorInfo error);

    /**
     * 节点信息
     */
    void onTrace(@NonNull ICmlInstance instance, @NonNull String event, @Nullable Map<String, Object> info);

}
