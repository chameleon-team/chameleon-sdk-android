package com.didi.chameleon.sdk.extend;

import android.text.TextUtils;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlCallbackSimple;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlModule;
import com.didi.chameleon.sdk.module.CmlParam;

@CmlModule(alias = "storage")
public class CmlStorageModule {

    @CmlMethod(alias = "setStorage", uiThread = false)
    public void setItem(@CmlParam(name = "key") String key, @CmlParam(name = "value") String value,
                        CmlCallbackSimple callback) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            callback.onFail();
            return;
        }
        CmlEnvironment.getLightStorage().save(key, value);
        callback.onSuccess();
    }

    @CmlMethod(alias = "getStorage", uiThread = false)
    public void getItem(@CmlParam(name = "key") String key, CmlCallback<String> callback) {
        if (TextUtils.isEmpty(key)) {
            callback.onError(CmlCallback.ERROR_DEFAULT);
            return;
        }
        String value = CmlEnvironment.getLightStorage().get(key);
        callback.onCallback(value);
    }

    @CmlMethod(alias = "removeStorage", uiThread = false)
    public void removeItem(@CmlParam(name = "key") String key, CmlCallbackSimple callback) {
        if (TextUtils.isEmpty(key)) {
            callback.onError(CmlCallback.ERROR_DEFAULT);
            return;
        }
        CmlEnvironment.getLightStorage().remove(key);
        callback.onSuccess();
    }
}
