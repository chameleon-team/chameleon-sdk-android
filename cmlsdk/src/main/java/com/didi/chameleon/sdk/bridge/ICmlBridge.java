package com.didi.chameleon.sdk.bridge;

import com.didi.chameleon.sdk.CmlEngine;

/**
 * 通道接口定义
 */
public interface ICmlBridge {
    /**
     * 初始化通道
     * <p>调用时机是在 {@link CmlEngine} init 时，主要业务是在初始化时将 Native 调用 JS 的工厂类
     * 注册到 {@link CmlBridgeManager}</p>
     */
    void init();
}
