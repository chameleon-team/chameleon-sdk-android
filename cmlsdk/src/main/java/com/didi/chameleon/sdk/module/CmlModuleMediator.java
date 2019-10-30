package com.didi.chameleon.sdk.module;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.ICmlActivityInstance;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.ICmlViewInstance;

import java.lang.reflect.Method;

public class CmlModuleMediator {

    public CmlModuleStore store;
    public CmlModuleFactory factory;
    public CmlModuleTrace trace;

    @NonNull
    public Object newInstance(String instanceId, @NonNull Class moduleClass)
            throws CmlModuleException {
        return factory.newModuleInstance(instanceId, moduleClass);
    }

    public Object newCallback(String instanceId, String callbackId, @Nullable Class callbackClass) {
        return factory.newCallbackInstance(instanceId, callbackId, callbackClass);
    }

    public String createCallbackId(String instanceId) {
        return trace.getUniqueCallbackId(instanceId);
    }

    public boolean isContextInfo(Class typeClass) {
        return typeClass == Context.class
                || typeClass == Activity.class
                || typeClass == ICmlInstance.class
                || typeClass == ICmlActivityInstance.class
                || typeClass == ICmlViewInstance.class;
    }

    public Object getContextInfo(String instanceId, Class typeClass) {
        if (typeClass == Context.class) {
            return trace.getContext(instanceId);
        } else if (typeClass == Activity.class) {
            return trace.getActivity(instanceId);
        } else if (typeClass == ICmlInstance.class) {
            return trace.getInstance(instanceId);
        } else if (typeClass == ICmlActivityInstance.class) {
            return trace.getActivityInstance(instanceId);
        } else if (typeClass == ICmlViewInstance.class) {
            return trace.getViewInstance(instanceId);
        }
        return null;
    }

    @NonNull
    public Info getInvokeInfo(@NonNull String instanceId, @NonNull String moduleName, @NonNull String methodName)
            throws CmlModuleException, CmlMethodException {
        return store.getMediatorInfo(instanceId, moduleName, methodName);
    }

    public CmlCallback removeCallback(@NonNull String instanceId, @NonNull String callbackId) {
        return store.dropCallback(instanceId, callbackId);
    }


    public static class Info {

        public Object object;
        public Method method;
        public String[] paramKey;
        public String[] paramAdmin;
        public boolean isUiThread;

    }


}
