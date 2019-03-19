package com.didi.chameleon.sdk.bridge;

import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Bridge 管理类
 * <p>Native 调用 JS 以及 JS 调用 Native 的 Bridge，在实例化时都会注册到这里，
 * 外部可根据 instanceId 来取用 Bridge进行双向通信</p>
 */
public class CmlBridgeManager {
    private static final String TAG = "CmlBridgeManager";

    private Map<Class, ICmlBridgeNativeToJsFactory> mBridgeNativeToJsFactories = new HashMap<>();
    private Map<String, ICmlBridgeNativeToJs> mNativeToJsBridges = new HashMap<>();
    private Map<String, ICmlBridgeJsToNative> mJsToNativeBridges = new HashMap<>();

    private static CmlBridgeManager instance = new CmlBridgeManager();

    private CmlBridgeManager() {
        CmlInstanceManage.getInstance().registerListener(new CmlInstanceManage.CmlInstanceChangeListener() {
            @Override
            public void onAddInstance(String instanceId) {

            }

            @Override
            public void onRemoveInstance(String instanceId) {
                mNativeToJsBridges.remove(instanceId);
                mJsToNativeBridges.remove(instanceId);
            }
        });
    }

    public static CmlBridgeManager getInstance() {
        return instance;
    }

    public void addBridgeNativeToJsFactory(Class clazz, ICmlBridgeNativeToJsFactory bridgeNativeToJsFactory) {
        mBridgeNativeToJsFactories.put(clazz, bridgeNativeToJsFactory);
    }

    public ICmlBridgeJsToNative getBridgeJsToNative(String instanceId) {
        return mJsToNativeBridges.get(instanceId);
    }

    public void registerJsToNativeListener(String instanceId, ICmlBridgeJsToNative bridgeJsToNative) {
        mJsToNativeBridges.put(instanceId, bridgeJsToNative);
    }

    public void invokeJsMethod(String instanceId, String module, String method, String args, String callbackId) {
        ICmlBridgeNativeToJs bridgeNativeToJs = mNativeToJsBridges.get(instanceId);
        if (null == bridgeNativeToJs) {
            bridgeNativeToJs = getBridgeNativeToJs(instanceId);
            if (null == bridgeNativeToJs) {
                CmlLogUtil.e(TAG, "invokeJsMethod: get bridge native to Js failed");
                return;
            }
        }
        bridgeNativeToJs.invokeJsMethod(module, method, args, callbackId);
    }

    public void callbackToJs(String instanceId, String args, String callbackId) {
        ICmlBridgeNativeToJs bridgeNativeToJs = mNativeToJsBridges.get(instanceId);
        if (null == bridgeNativeToJs) {
            bridgeNativeToJs = getBridgeNativeToJs(instanceId);
            if (null == bridgeNativeToJs) {
                CmlLogUtil.e(TAG, "callbackToJs: get bridge native to Js failed");
                return;
            }
        }
        bridgeNativeToJs.callbackToJs(args, callbackId);
    }

    private ICmlBridgeNativeToJs getBridgeNativeToJs(String instanceId) {
        ICmlBridgeNativeToJs bridgeNativeToJs = null;
        ICmlInstance instance = CmlInstanceManage.getInstance().getCmlInstance(instanceId);
        ICmlBridgeNativeToJsFactory nativeToJsFactory = mBridgeNativeToJsFactories.get(instance.getClass());
        if (null != nativeToJsFactory) {
            bridgeNativeToJs = nativeToJsFactory.getBridgeNativeToJs(instanceId);
            mNativeToJsBridges.put(instanceId, bridgeNativeToJs);
        }
        return bridgeNativeToJs;
    }
}
