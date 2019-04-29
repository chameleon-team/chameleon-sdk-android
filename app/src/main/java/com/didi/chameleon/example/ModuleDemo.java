package com.didi.chameleon.example;

import android.widget.Toast;

import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlModule;
import com.didi.chameleon.sdk.module.CmlParam;

@CmlModule(alias = "moduleDemo")
public class ModuleDemo {
    @CmlMethod(alias = "sayHello")
    public void sayHello(ICmlInstance instance, @CmlParam(name = "content") String content, CmlCallback callback) {
        Toast.makeText(instance.getContext(), content, Toast.LENGTH_SHORT).show();
        if (null != callback) {
            callback.onCallback("callback data 123.");
        }
    }
}
