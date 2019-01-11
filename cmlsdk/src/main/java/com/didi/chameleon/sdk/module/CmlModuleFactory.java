package com.didi.chameleon.sdk.module;

import android.support.annotation.NonNull;

public class CmlModuleFactory {

    @NonNull
    public Object newCallbackInstance(String instanceId, String callbackId, Class callbackClass) {
        if (callbackClass == CmlCallback.class) {
            return new CmlCallbackAction<>(Object.class, instanceId, callbackId);
        } else if (callbackClass == CmlCallbackSimple.class) {
            return new CmlCallbackSimple(instanceId, callbackId);
        } else {
            throw new IllegalStateException();
        }
    }

    @NonNull
    public Object newModuleInstance(String instanceId, Class moduleClass) throws CmlModuleException {
        Object object;
        try {
            object = moduleClass.newInstance();
        } catch (InstantiationException e) {
            throw CmlModuleException.throwInitFail(moduleClass, e);
        } catch (IllegalAccessException e) {
            throw CmlModuleException.throwInitFail(moduleClass, e);
        }
        return object;
    }

}
