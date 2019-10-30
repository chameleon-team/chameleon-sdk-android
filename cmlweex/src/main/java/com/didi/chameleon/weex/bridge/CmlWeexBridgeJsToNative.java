package com.didi.chameleon.weex.bridge;

import android.net.Uri;

import com.didi.chameleon.sdk.bridge.CmlBridgeManager;
import com.didi.chameleon.sdk.bridge.CmlProtocol;
import com.didi.chameleon.sdk.bridge.CmlProtocolProcessor;
import com.didi.chameleon.sdk.bridge.ICmlBridgeJsToNative;
import com.didi.chameleon.sdk.bridge.ICmlBridgeProtocol;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

public class CmlWeexBridgeJsToNative extends WXModule implements ICmlBridgeJsToNative {
    private static final String TAG = "CmlWeexBridgeJsToNative";

    private String mInstanceId;

    @JSMethod(alias = ICmlBridgeProtocol.CML_BRIDGE_METHOD)
    public void channel(String protocol) {
        mInstanceId = mWXSDKInstance.getInstanceId();

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
}
