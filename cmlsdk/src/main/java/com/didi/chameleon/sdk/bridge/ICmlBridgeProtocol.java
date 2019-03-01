package com.didi.chameleon.sdk.bridge;

public interface ICmlBridgeProtocol {
    String JS_CHANNEL_DEFINE = "CmlBridge.js";

    String CML_SCHEME = "cml://";
    String CML_BRIDGE = "cmlBridge";
    String CML_BRIDGE_METHOD = "channel";
    String CML_BRIDGE_EVENT = "cmlBridgeChannel";

    String KEY_ACTION = "action";
    String KEY_MODULE = "module";
    String KEY_METHOD = "method";
    String KEY_ARGS = "args";
    String KEY_CALLBACK_ID = "callbackId";
    String ACTION_INVOKE_JS_METHOD = "invokeJsMethod";
    String ACTION_CALLBACK_TO_JS = "callbackToJs";
    String ACTION_INVOKE_NATIVE_METHOD = "invokeNativeMethod";
    String ACTION_CALLBACK_FROM_JS = "callbackFromJs";

    // native -> js
    String CML_JS_CHANNEL_METHOD = "cmlBridge.channel"; //对应的js方法
    String CML_JS_CHANNEL = "javascript:" + CML_JS_CHANNEL_METHOD + "('%s');";
}
