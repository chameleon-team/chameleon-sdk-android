package com.didi.chameleon.example;

import android.widget.Toast;

import com.didi.chameleon.sdk.ICmlActivityInstance;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlModule;
import com.didi.chameleon.sdk.module.CmlParam;

@CmlModule(alias = "moduleDemo")
public class ModuleDemo {
    @CmlMethod(alias = "sayHello")
    public void sayHello(ICmlActivityInstance instance, @CmlParam(name = "content") String content) {
        Toast.makeText(instance.getContext(), content, Toast.LENGTH_SHORT);
    }
}
