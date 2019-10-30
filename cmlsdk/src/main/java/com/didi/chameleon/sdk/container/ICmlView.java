package com.didi.chameleon.sdk.container;

import com.didi.chameleon.sdk.ICmlBaseLifecycle;
import com.didi.chameleon.sdk.module.CmlCallback;

import java.util.HashMap;

public interface ICmlView extends ICmlBaseLifecycle, ICmlContainer {
    /**
     * @param url     jsBundle 地址
     * @param options 需要透传的参数
     */
    void render(String url, HashMap<String, Object> options);

    /**
     * native 主动调用 js 侧方法
     *
     * @param module   模块名
     * @param method   方法名
     * @param args     参数
     * @param callback 如果需要js回调native则需要此对象
     */
    void invokeJsMethod(String module, String method, String args, CmlCallback callback);
}
