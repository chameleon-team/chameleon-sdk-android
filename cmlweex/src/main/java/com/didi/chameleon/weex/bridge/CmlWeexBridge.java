package com.didi.chameleon.weex.bridge;

import com.didi.chameleon.sdk.bridge.CmlBridgeManager;
import com.didi.chameleon.sdk.bridge.ICmlBridge;
import com.didi.chameleon.sdk.bridge.ICmlBridgeNativeToJs;
import com.didi.chameleon.sdk.bridge.ICmlBridgeNativeToJsFactory;
import com.didi.chameleon.weex.CmlWeexInstance;
import com.didi.chameleon.weex.CmlWeexViewInstance;

public class CmlWeexBridge implements ICmlBridge, ICmlBridgeNativeToJsFactory {
    private static final String TAG = "CmlWeexBridge";

    @Override
    public void init() {
        CmlBridgeManager.getInstance().addBridgeNativeToJsFactory(CmlWeexInstance.class, this);
        CmlBridgeManager.getInstance().addBridgeNativeToJsFactory(CmlWeexViewInstance.class, this);
    }

    public ICmlBridgeNativeToJs getBridgeNativeToJs(String instanceId) {
        return new CmlWeexBridgeNativeToJs(instanceId);
    }
}
