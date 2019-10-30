package com.didi.chameleon.sdk.bridge;

/**
 * Chameleon bridge protocol, native -> js <p/>
 * invokeJsMethod example: <br />
 * cml://channel?action=invokeJsMethod&module=xxx&method=getToken&args=xxx&callbackId=xxx <p/>
 * callbackToJs example: <br />
 * cml://channel?action=callbackToJs&args=xxx&callbackId=xxx
 */
public interface ICmlBridgeNativeToJs extends ICmlBridgeProtocol {
    /**
     * native Native 主动调用 JS 侧方法
     * @param module        模块名称
     * @param method        方法名称
     * @param args          方法参数，json 格式
     * @param callbackId    可选，回调标识。如果需要 JS 处理完业务再回调 native，则需要此标识，JS 在回调时回传给 native
     */
    void invokeJsMethod(String module, String method, String args, String callbackId);

    /**
     * native 回调 JS 侧
     * @param args          回调参数，json 格式
     * @param callbackId    回调标识
     */
    void callbackToJs(String args, String callbackId);
}
