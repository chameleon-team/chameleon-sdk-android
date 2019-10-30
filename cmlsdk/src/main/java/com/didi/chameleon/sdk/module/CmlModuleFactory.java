package com.didi.chameleon.sdk.module;

import android.support.annotation.NonNull;

import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
            Constructor constructor = moduleClass.getConstructors()[0];
            switch (constructor.getParameterTypes().length) {
                case 0:
                    object = moduleClass.newInstance();
                    break;
                case 1:
                    if (instanceId == null) {
                        CmlLogUtil.e("CmlModuleFactory", "if module is not global,it can't init has instance");
                    }
                    object = constructor.newInstance(CmlInstanceManage.getInstance().getCmlInstance(instanceId));
                    break;
                default:
                    throw CmlModuleException.throwInitFail(moduleClass, new IllegalAccessError());
            }
        } catch (InstantiationException e) {
            throw CmlModuleException.throwInitFail(moduleClass, e);
        } catch (IllegalAccessException e) {
            throw CmlModuleException.throwInitFail(moduleClass, e);
        } catch (InvocationTargetException e) {
            throw CmlModuleException.throwInitFail(moduleClass, e);
        }
        for (Class interfaceClass : moduleClass.getInterfaces()) {
            if (interfaceClass == ICmlModuleDestroy.class) {
                CmlModuleDestroyWrapper.register(instanceId, (ICmlModuleDestroy) object);
            }
        }
        return object;
    }

}
