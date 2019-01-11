package com.didi.chameleon.web.bridge;

import com.didi.chameleon.sdk.bridge.CmlBridgeManager;
import com.didi.chameleon.sdk.bridge.ICmlBridge;
import com.didi.chameleon.sdk.bridge.ICmlBridgeNativeToJs;
import com.didi.chameleon.sdk.bridge.ICmlBridgeNativeToJsFactory;
import com.didi.chameleon.web.CmlWebInstance;


public class CmlWebBridge implements ICmlBridge, ICmlBridgeNativeToJsFactory {
    private static final String TAG = "CmlWebBridge";

    @Override
    public void init() {
        CmlBridgeManager.getInstance().addBridgeNativeToJsFactory(CmlWebInstance.class, this);
    }

    /**
     * 生产 Native 调用 JS 的对象
     * 当业务层需要主动和 js 侧通信时，传入一个 instanceId，如果此 instanceId 对应的通道不存在，则会
     * @param instanceId
     * @return
     */
    public ICmlBridgeNativeToJs getBridgeNativeToJs(String instanceId) {
        return new CmlWebBridgeNativeToJs(instanceId);
    }
}
