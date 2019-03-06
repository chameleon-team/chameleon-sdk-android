package com.didi.chameleon.sdk;

import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.container.ICmlContainer;

/**
 * Instance 需要提供的基础能力
 */
public interface ICmlInstance extends ICmlContainer {

    String getInstanceId();

    /**
     * 获取初始目标URL
     *
     * @return URL
     */
    @Nullable
    String getTargetURL();

    /**
     * 获取当前页面URL
     *
     * @return URL
     */
    @Nullable
    String getCurrentURL();

    /**
     * 实现回传数据功能
     *
     * @param protocol
     */
    void nativeToJs(String protocol);

    /**
     * 重新加载当前页面
     *
     * @param url
     */
    void reload(String url);

    /**
     * 降级到H5页面
     *
     * @param degradeCode 降级原因，参考 {@link CmlConstant}
     */
    void degradeToH5(int degradeCode);

    /**
     * 主页面调起子页面，子页面和主页面通信
     *
     * @param resultCode
     * @param result
     */
    void onResult(int resultCode, String result);
}
