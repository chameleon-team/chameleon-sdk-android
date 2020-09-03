package com.didi.chameleon.sdk.adapter.monitor;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.ICmlInstance;

import java.util.Map;

public interface ICmlMonitorAdapter {

    String TRACE_PERFORMANCE = "performance";
    String TRACE_RENDER_LOAD = "render_load";
    String TRACE_RENDER_SUCCESS = "render_success";
    String TRACE_RENDER_FAIL = "render_fail";

    String PARAM_LOAD_SOURCE = "load_source";
    String PARAM_RENDER_TIME = "render_time";

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
