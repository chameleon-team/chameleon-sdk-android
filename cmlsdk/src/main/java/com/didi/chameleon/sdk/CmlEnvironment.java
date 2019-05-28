package com.didi.chameleon.sdk;

import com.didi.chameleon.sdk.adapter.CmlDefaultImgLoaderAdapter;
import com.didi.chameleon.sdk.adapter.ICmlDegradeAdapter;
import com.didi.chameleon.sdk.adapter.ICmlImgLoaderAdapter;
import com.didi.chameleon.sdk.adapter.ICmlStatisticsAdapter;
import com.didi.chameleon.sdk.adapter.http.ICmlHttpAdapter;
import com.didi.chameleon.sdk.adapter.json.ICmlJsonAdapter;
import com.didi.chameleon.sdk.adapter.log.CmlLoggerDefault;
import com.didi.chameleon.sdk.adapter.log.ICmlLoggerAdapter;
import com.didi.chameleon.sdk.adapter.modal.ICmlDialogAdapter;
import com.didi.chameleon.sdk.adapter.modal.ICmlProgressAdapter;
import com.didi.chameleon.sdk.adapter.modal.ICmlToastAdapter;
import com.didi.chameleon.sdk.adapter.navigator.CmlNavigatorDefault;
import com.didi.chameleon.sdk.adapter.navigator.ICmlNavigatorAdapter;
import com.didi.chameleon.sdk.adapter.storage.ICmlStorageAdapter;
import com.didi.chameleon.sdk.adapter.thread.ICmlThreadAdapter;
import com.didi.chameleon.sdk.adapter.websocket.CmlWebSocketDefault;
import com.didi.chameleon.sdk.adapter.websocket.ICmlWebSocketAdapter;
import com.didi.chameleon.sdk.common.CmlJsonWrapper;
import com.didi.chameleon.sdk.common.CmlLightStorage;
import com.didi.chameleon.sdk.common.CmlModalTip;
import com.didi.chameleon.sdk.common.CmlThreadCenter;
import com.didi.chameleon.sdk.common.http.CmlStreamHttp;

/**
 * 配置类
 * <p>CmlEnvironment 主要提供了开发期间需要的一些配置能力，一些常量定义。
 * 一个非常重要的能力是提供了对 adapter 设置和获取能力，方便使用者实现自己的适配模块。</p>
 */
public class CmlEnvironment {
    /**
     * SDK版本
     */
    public static final String VERSION = "0.0.10";
    /**
     * 调试开关
     */
    public static boolean CML_DEBUG = false;
    /**
     * 页面是否直接降级
     */
    public static boolean CML_DEGRADE = false;
    /**
     * 是否开启缓存。前端调试的时候需要关闭缓存
     */
    public static boolean CML_ALLOW_BUNDLE_CACHE = true;

    /**
     * 是否输出分析日志
     */
    public static boolean CML_OUTPUT_STATISTICS = false;
    /**
     * sdk环境tag,与url
     */
    public static final String CML_QUERY_SDK = "cml_sdk";
    public static final String CML_QUERY_URL = "cml_url";

    /**
     * 预加载的最大缓存
     */
    private static long maxPreloadSize = 4 * 1024 * 1024;
    /**
     * 运行时的最大缓存
     */
    private static long maxRuntimeSize = 4 * 1024 * 1024;

    private static ICmlJsonAdapter jsonAdapter;
    private static ICmlThreadAdapter threadAdapter;
    private static ICmlStorageAdapter storageAdapter;
    private static ICmlLoggerAdapter loggerAdapter;
    private static ICmlWebSocketAdapter webSocketAdapter;
    private static ICmlHttpAdapter httpAdapter;
    private static ICmlToastAdapter toastAdapter;
    private static ICmlDialogAdapter dialogAdapter;
    private static ICmlProgressAdapter progressAdapter;

    private static ICmlNavigatorAdapter navigatorAdapter;
    private static ICmlStatisticsAdapter statisticsAdapter;
    private static ICmlDegradeAdapter degradeAdapter;
    private static ICmlImgLoaderAdapter imageLoaderAdapter;

    private static CmlJsonWrapper jsonWrapper;
    private static CmlThreadCenter threadCenter;
    private static CmlLightStorage lightStorage;
    private static CmlStreamHttp streamHttp;
    private static CmlModalTip modalTip;

    public static void setJsonAdapter(ICmlJsonAdapter jsonAdapter) {
        CmlEnvironment.jsonAdapter = jsonAdapter;
        jsonWrapper = null;
    }

    public static void setThreadAdapter(ICmlThreadAdapter threadAdapter) {
        CmlEnvironment.threadAdapter = threadAdapter;
        threadCenter = null;
    }

    public static void setStorageAdapter(ICmlStorageAdapter storageAdapter) {
        CmlEnvironment.storageAdapter = storageAdapter;
        lightStorage = null;
    }

    public static void setLoggerAdapter(ICmlLoggerAdapter loggerAdapter) {
        CmlEnvironment.loggerAdapter = loggerAdapter;
    }

    public static void setWebSocketAdapter(ICmlWebSocketAdapter webSocketAdapter) {
        CmlEnvironment.webSocketAdapter = webSocketAdapter;
    }

    public static void setHttpAdapter(ICmlHttpAdapter httpAdapter) {
        CmlEnvironment.httpAdapter = httpAdapter;
        streamHttp = null;
    }

    public static void setToastAdapter(ICmlToastAdapter toastAdapter) {
        CmlEnvironment.toastAdapter = toastAdapter;
        modalTip = null;
    }

    public static void setDialogAdapter(ICmlDialogAdapter dialogAdapter) {
        CmlEnvironment.dialogAdapter = dialogAdapter;
        modalTip = null;
    }

    public static void setProgressAdapter(ICmlProgressAdapter progressAdapter) {
        CmlEnvironment.progressAdapter = progressAdapter;
        modalTip = null;
    }

    public static void setStatisticsAdapter(ICmlStatisticsAdapter statisticsAdapter) {
        CmlEnvironment.statisticsAdapter = statisticsAdapter;
    }

    public static void setNavigatorAdapter(ICmlNavigatorAdapter navigatorAdapter) {
        CmlEnvironment.navigatorAdapter = navigatorAdapter;
    }

    public static void setDegradeAdapter(ICmlDegradeAdapter degradeAdapter) {
        CmlEnvironment.degradeAdapter = degradeAdapter;
    }

    public static void setImageLoaderAdapter(ICmlImgLoaderAdapter imageLoaderAdapter) {
        CmlEnvironment.imageLoaderAdapter = imageLoaderAdapter;
    }

    public static long getMaxPreloadSize() {
        return maxPreloadSize;
    }

    public static long getMaxRuntimeSize() {
        return maxRuntimeSize;
    }

    /**
     * 设置预加载的最大缓存
     */
    public static void setMaxPreloadSize(long maxPreloadSize) {
        CmlEnvironment.maxPreloadSize = maxPreloadSize;
    }

    /**
     * 设置运行时的最大缓存
     */
    public static void setMaxRuntimeSize(long maxRuntimeSize) {
        CmlEnvironment.maxRuntimeSize = maxRuntimeSize;
    }

    public static CmlJsonWrapper getJsonWrapper() {
        if (jsonWrapper == null) {
            jsonWrapper = new CmlJsonWrapper(jsonAdapter);
        }
        return jsonWrapper;
    }

    public static CmlThreadCenter getThreadCenter() {
        if (threadCenter == null) {
            threadCenter = new CmlThreadCenter(threadAdapter);
        }
        return threadCenter;
    }

    public static CmlLightStorage getLightStorage() {
        if (lightStorage == null) {
            lightStorage = new CmlLightStorage(CmlEngine.getInstance().getAppContext(), storageAdapter);
        }
        return lightStorage;
    }

    public static ICmlLoggerAdapter getLoggerAdapter() {
        if (loggerAdapter == null) {
            loggerAdapter = new CmlLoggerDefault();
        }
        return loggerAdapter;
    }

    public static ICmlNavigatorAdapter getNavigatorAdapter() {
        if (navigatorAdapter == null) {
            navigatorAdapter = new CmlNavigatorDefault();
        }
        return navigatorAdapter;
    }

    public static synchronized ICmlStatisticsAdapter getStatisticsAdapter() {
        return statisticsAdapter;
    }

    public static ICmlDegradeAdapter getDegradeAdapter() {
        return degradeAdapter;
    }

    public static ICmlImgLoaderAdapter getImgLoaderAdapter() {
        if (imageLoaderAdapter == null) {
            imageLoaderAdapter = new CmlDefaultImgLoaderAdapter();
        }
        return imageLoaderAdapter;
    }

    public static ICmlWebSocketAdapter getWebSocketAdapter() {
        if (webSocketAdapter == null) {
            webSocketAdapter = new CmlWebSocketDefault();
        }
        return webSocketAdapter;
    }

    public static CmlStreamHttp getStreamHttp() {
        if (streamHttp == null) {
            streamHttp = new CmlStreamHttp(httpAdapter);
        }
        return streamHttp;
    }

    public static CmlModalTip getModalTip() {
        if (modalTip == null) {
            modalTip = new CmlModalTip(toastAdapter, dialogAdapter, progressAdapter);
        }
        return modalTip;
    }
}
