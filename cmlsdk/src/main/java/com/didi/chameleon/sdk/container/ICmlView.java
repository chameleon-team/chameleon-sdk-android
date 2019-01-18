package com.didi.chameleon.sdk.container;

import com.didi.chameleon.sdk.ICmlBaseLifecycle;

import java.util.HashMap;

public interface ICmlView extends ICmlBaseLifecycle, ICmlContainer {
    /**
     * @param url     jsBundle 地址
     * @param options 需要透传的参数
     */
    void render(String url, HashMap<String, Object> options);
}
