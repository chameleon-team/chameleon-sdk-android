package com.didi.chameleon.sdk.bridge;

import android.net.Uri;
import android.text.TextUtils;

import com.didi.chameleon.sdk.utils.CmlLogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CmlProtocolProcessor implements ICmlBridgeProtocol {
    private static final String TAG = "CmlProtocolProcessor";

    public static CmlProtocol parserProtocol(Uri uri) {
        String action = uri.getQueryParameter(KEY_ACTION);
        String module = uri.getQueryParameter(KEY_MODULE);
        String method = uri.getQueryParameter(KEY_METHOD);
        String args = uri.getQueryParameter(KEY_ARGS);
        try {
            args = URLDecoder.decode(args, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String callbackId = uri.getQueryParameter(KEY_CALLBACK_ID);

        CmlProtocol protocol = new CmlProtocol();
        protocol.setAction(action);
        protocol.setModule(module);
        protocol.setMethod(method);
        protocol.setArgs(args);
        protocol.setCallbackId(callbackId);
        return protocol;
    }

    public static String invokeJsMethod(String module, String method, String args, String callbackId) {
        StringBuffer protocol = new StringBuffer();

        try {
            protocol.append(CML_SCHEME).append(CML_BRIDGE_METHOD);
            protocol.append("?").append(KEY_ACTION).append("=").append(ACTION_INVOKE_JS_METHOD);
            protocol.append("&").append(KEY_MODULE).append("=").append(module);
            protocol.append("&").append(KEY_METHOD).append("=").append(method);
            //考虑参数中含有关键字，需要encode获取参数，然后再解码
            if (!TextUtils.isEmpty(args)) {
                protocol.append("&").append(KEY_ARGS).append("=").append(URLEncoder.encode(args, "UTF-8"));
            }
            if (!TextUtils.isEmpty(callbackId)) {
                protocol.append("&").append(KEY_CALLBACK_ID).append("=").append(callbackId);
            }
        } catch (UnsupportedEncodingException e) {
            CmlLogUtil.e(TAG, "invokeJsMethod encode error.");
        }

        return protocol.toString();
    }

    public static String callbackToJs(String args, String callbackId) {
        StringBuffer protocol = new StringBuffer();

        try {
            protocol.append(CML_SCHEME).append(CML_BRIDGE_METHOD);
            protocol.append("?").append(KEY_ACTION).append("=").append(ACTION_CALLBACK_TO_JS);
            if (!TextUtils.isEmpty(args)) {
                protocol.append("&").append(KEY_ARGS).append("=").append(URLEncoder.encode(args, "UTF-8"));
            }
            protocol.append("&").append(KEY_CALLBACK_ID).append("=").append(callbackId);
        } catch (UnsupportedEncodingException e) {
            CmlLogUtil.e(TAG, "invokeJsMethod encode error.");
        }

        return protocol.toString();
    }

    public static String actionErrMsg() {
        // invokeNativeMethod | callbackFromJs
        StringBuffer errMsg = new StringBuffer();
        errMsg.append(ACTION_INVOKE_NATIVE_METHOD);
        errMsg.append(" | ").append(ACTION_CALLBACK_FROM_JS);
        return errMsg.toString();
    }
}
