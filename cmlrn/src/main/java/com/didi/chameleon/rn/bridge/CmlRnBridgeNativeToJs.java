package com.didi.chameleon.rn.bridge;

import android.text.TextUtils;

import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.bridge.CmlProtocolProcessor;
import com.didi.chameleon.sdk.bridge.ICmlBridgeNativeToJs;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.facebook.react.bridge.ReactApplicationContext;

public class CmlRnBridgeNativeToJs implements ICmlBridgeNativeToJs {
    private static final String TAG = "CmlRnBridgeNativeToJs";
    private ReactApplicationContext mReactContext;
    ICmlInstance mCmlInstance;

    public CmlRnBridgeNativeToJs(ReactApplicationContext reactContext, String instanceId) {
        mReactContext = reactContext;
        mCmlInstance = CmlInstanceManage.getInstance().getCmlInstance(instanceId);
    }

    @Override
    public void invokeJsMethod(String module, String method, String args, String callbackId) {
        if (TextUtils.isEmpty(module)) {
            CmlLogUtil.e(TAG, "module name can not be empty");
            return;
        }
        if (TextUtils.isEmpty(method)) {
            CmlLogUtil.e(TAG, "method name can not be empty");
            return;
        }
        if (null != mCmlInstance) {
            mCmlInstance.nativeToJs(CmlProtocolProcessor.invokeJsMethod(module, method, args, callbackId));
        }
    }

    @Override
    public void callbackToJs(String args, String callbackId) {
        if (TextUtils.isEmpty(callbackId)) {
            CmlLogUtil.e(TAG, "callbackId can not be empty");
            return;
        }
        if (null != mCmlInstance) {
            mCmlInstance.nativeToJs(CmlProtocolProcessor.callbackToJs(args, callbackId));
        }
    }
}
