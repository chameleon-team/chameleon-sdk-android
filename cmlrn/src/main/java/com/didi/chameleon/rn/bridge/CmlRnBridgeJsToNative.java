package com.didi.chameleon.rn.bridge;

import android.net.Uri;

import com.didi.chameleon.rn.CmlRnInstance;
import com.didi.chameleon.sdk.bridge.CmlBridgeManager;
import com.didi.chameleon.sdk.bridge.CmlProtocol;
import com.didi.chameleon.sdk.bridge.CmlProtocolProcessor;
import com.didi.chameleon.sdk.bridge.ICmlBridgeJsToNative;
import com.didi.chameleon.sdk.bridge.ICmlBridgeNativeToJs;
import com.didi.chameleon.sdk.bridge.ICmlBridgeNativeToJsFactory;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class CmlRnBridgeJsToNative extends ReactContextBaseJavaModule implements ICmlBridgeNativeToJsFactory, ICmlBridgeJsToNative {
    private static final String TAG = "CmlRnBridgeJsToNative";
    private String mInstanceId;

    public CmlRnBridgeJsToNative(ReactApplicationContext reactContext, String instanceId) {
        super(reactContext);
        mInstanceId = instanceId;
        CmlBridgeManager.getInstance().addBridgeNativeToJsFactory(CmlRnInstance.class, this);
    }

    @Override
    public String getName() {
        return CML_BRIDGE;
    }

    @ReactMethod
    public void channel(String protocol) {
        if (protocol.startsWith(CML_SCHEME)) {
            CmlLogUtil.i(TAG, "origin protocl: " + protocol);
            Uri uri = Uri.parse(protocol);
            CmlProtocol cmlProtocol = CmlProtocolProcessor.parserProtocol(uri);
            if (cmlProtocol.isActionEmpty() || cmlProtocol.isModuleEmpty() || cmlProtocol.isMethodEmpty()) {
                CmlLogUtil.e(TAG, "bridge protocol error:  " + protocol);
                return;
            }

            switch (cmlProtocol.getAction()) {
                case ACTION_INVOKE_NATIVE_METHOD:
                    invokeNativeMethod(mInstanceId,
                            cmlProtocol.getModule(),
                            cmlProtocol.getMethod(),
                            cmlProtocol.getArgs(),
                            cmlProtocol.getCallbackId());
                    break;
                case ACTION_CALLBACK_FROM_JS:
                    callbackFromJs(mInstanceId,
                            cmlProtocol.getArgs(),
                            cmlProtocol.getCallbackId());
                    break;
                default:
                    CmlLogUtil.e(TAG, "bridge action must be " + CmlProtocolProcessor.actionErrMsg());
                    break;
            }
        } else {
            CmlLogUtil.e(TAG, "protocl error: " + protocol);
        }
    }

    @Override
    public void invokeNativeMethod(String instanceId, String module, String method, String args, String callbackId) {
        ICmlBridgeJsToNative bridge = CmlBridgeManager.getInstance().getBridgeJsToNative(instanceId);
        if (null != bridge) {
            bridge.invokeNativeMethod(instanceId, module, method, args, callbackId);
        }
    }

    @Override
    public void callbackFromJs(String instanceId, String args, String callbackId) {
        ICmlBridgeJsToNative bridge = CmlBridgeManager.getInstance().getBridgeJsToNative(instanceId);
        if (null != bridge) {
            bridge.callbackFromJs(instanceId, args, callbackId);
        }
    }

    public ICmlBridgeNativeToJs getBridgeNativeToJs(String instanceId) {
        return new CmlRnBridgeNativeToJs(getReactApplicationContext(), instanceId);
    }
}
