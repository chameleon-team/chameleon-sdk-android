package com.didi.chameleon.sdk.bridge;

/**
 * Chameleon bridge protocol, js -> native <p/>
 * invokeNativeMethod example: <br />
 * cml://channel?action=invokeNativeMethod&module=xxx&method=takePhoto&args=xxx&callbackId=xxx <p/>
 * callbackFromJs example: <br />
 * cml:channel//?action=callbackFromJs&args=xxx&callbackId=xxx
 */
public interface ICmlBridgeJsToNative extends ICmlBridgeProtocol {
    /**
     * native JS 主动调用 native 侧方法
     * @param module        模块名称
     * @param method        方法名称
     * @param args          方法参数，json 格式
     * @param callbackId    可选，回调标识。如果需要 JS 处理完业务再回调 native，则需要此标识，JS 在回调时回传给 native
     */
    void invokeNativeMethod(String instanceId, String module, String method, String args, String callbackId);

    /**
     * native 回调 Native 侧
     * @param args          回调参数，json 格式
     * @param callbackId    回调标识
     */
    void callbackFromJs(String instanceId, String args, String callbackId);
}
