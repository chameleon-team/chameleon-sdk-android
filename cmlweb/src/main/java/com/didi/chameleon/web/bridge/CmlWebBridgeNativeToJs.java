package com.didi.chameleon.web.bridge;

import android.text.TextUtils;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.bridge.CmlProtocolProcessor;
import com.didi.chameleon.sdk.bridge.ICmlBridgeNativeToJs;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

public class CmlWebBridgeNativeToJs implements ICmlBridgeNativeToJs {
    private static final String TAG = "CmlRnBridgeNativeToJs";
    private ICmlInstance mInstance;

    public CmlWebBridgeNativeToJs(String instanceId) {
        mInstance = CmlInstanceManage.getInstance().getCmlInstance(instanceId);
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
        sendMsg(CmlProtocolProcessor.invokeJsMethod(module, method, args, callbackId));
    }

    @Override
    public void callbackToJs(String args, String callbackId) {
        if (TextUtils.isEmpty(callbackId)) {
            CmlLogUtil.e(TAG, "callbackId can not be empty");
            return;
        }
        sendMsg(CmlProtocolProcessor.callbackToJs(args, callbackId));
    }

    private void sendMsg(String protocol) {
        final String jsUrl = String.format(CML_JS_CHANNEL, protocol);

        // 必须要找主线程才会将数据传递出去 --- 划重点
        CmlEnvironment.getThreadCenter().postMain(new Runnable() {
            @Override
            public void run() {
                mInstance.nativeToJs(jsUrl);
            }
        });
    }
}
