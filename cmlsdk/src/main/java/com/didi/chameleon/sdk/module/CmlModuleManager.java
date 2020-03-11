package com.didi.chameleon.sdk.module;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.bridge.CmlBridgeManager;
import com.didi.chameleon.sdk.bridge.ICmlBridgeJsToNative;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

public class CmlModuleManager {

    private static final String TAG = "CmlModuleManager";

    private static CmlModuleManager instance = new CmlModuleManager();

    public static CmlModuleManager getInstance() {
        return instance;
    }

    private CmlModuleMediator mediator = new CmlModuleMediator();
    private CmlModuleStore store = new CmlModuleStore(mediator);
    private CmlModuleInvoke invoke = new CmlModuleInvoke(mediator);
    private CmlModuleFactory factory = new CmlModuleFactory();
    private CmlModuleTrace trace = new CmlModuleTrace();

    private CmlModuleManager() {
        mediator.factory = factory;
        mediator.store = store;
        mediator.trace = trace;
        CmlInstanceManage.getInstance().registerListener(new CmlInstanceManage.CmlInstanceChangeListener() {
            @Override
            public void onAddInstance(String instanceId) {
                registerInstance(instanceId);
            }

            @Override
            public void onRemoveInstance(String instanceId) {
                store.removeInstance(instanceId);
            }
        });
    }

    private void registerInstance(String instanceId) {
        CmlBridgeManager.getInstance().registerJsToNativeListener(instanceId, new ICmlBridgeJsToNative() {
            @Override
            public void invokeNativeMethod(String instanceId, String module, String method, String args, String callbackId) {
                invokeNative(instanceId, module, method, args, callbackId);
            }

            @Override
            public void callbackFromJs(String instanceId, String args, String callbackId) {
                callbackNative(instanceId, args, callbackId);
            }
        });
    }

    public void addCmlModule(Class moduleClass) {
        try {
            store.storeModule(moduleClass);
        } catch (Throwable e) {
            // 兼容三星手机获取注解失败
            if (CmlEnvironment.CML_DEBUG) {
                throw e;
            } else {
                CmlLogUtil.et(e);
            }
        }
    }

    public boolean containsAction(@NonNull String moduleName, @NonNull String methodName) {
        return store.containsAction(moduleName, methodName);
    }

    public void invokeNative(@NonNull String instanceId, @NonNull String moduleName, @NonNull String methodName,
                             @Nullable String params, @Nullable String callbackId) {
        CmlLogUtil.d(TAG, "invokeNative: " + instanceId + " " + moduleName + " " + methodName + " " + callbackId + " " + params);
        try {
            invoke.invokeNative(instanceId, moduleName, methodName, params, callbackId);
        } catch (CmlModuleException e) {
            e.printStackTrace();
        } catch (CmlMethodException e) {
            e.printStackTrace();
        }
    }

    public void callbackNative(@NonNull String instanceId, @NonNull String callbackId, @Nullable String params) {
        CmlLogUtil.d(TAG, "callbackNative: " + instanceId + " " + callbackId + " " + params);
        invoke.callbackNative(instanceId, callbackId, params);
    }

    public <T> void invokeWeb(@NonNull String instanceId, @NonNull String moduleName, @NonNull String methodName,
                              @Nullable Object params, @Nullable CmlCallback<T> callback) {
        String callbackId = null;
        if (callback != null) {
            callbackId = store.stashCallback(instanceId, callback);
        }
        String paramStr = invoke.wrapperParam(params);
        CmlLogUtil.d(TAG, "invokeWeb: " + instanceId + " " + moduleName + " " + methodName + " " + callbackId + " " + paramStr);
        CmlBridgeManager.getInstance().invokeJsMethod(instanceId, moduleName, methodName, paramStr, callbackId);
    }

    public <T> void callbackWeb(@NonNull String instanceId, @NonNull String callbackId, @Nullable CmlCallbackModel<T> params) {
        String paramStr = null;
        try {
            paramStr = invoke.wrapperCallback(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CmlLogUtil.d(TAG, "callbackWeb: " + instanceId + " " + callbackId + " " + paramStr);
        CmlBridgeManager.getInstance().callbackToJs(instanceId, paramStr, callbackId);
    }

}
