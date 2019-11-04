package com.didi.chameleon.example;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.ICmlConfig;
import com.didi.chameleon.sdk.bridge.CmlProtocolProcessor;
import com.taobao.weex.WXEnvironment;

public class MyApplication extends Application implements ICmlConfig {
    @Override
    public void onCreate() {
        super.onCreate();

//        initDebugEnvironment(true,false,
//                "https://xx/devtool_fake.html?_wx_devtool=ws://xx");

        CmlEngine.getInstance().init(this, this);

        String result = CmlProtocolProcessor.invokeJsMethod("mo","me","arg1 arg2 arg3","1");
        Log.e("lzc",result);

        String result2 = CmlProtocolProcessor.callbackToJs("arg3 arg4 arg5","2");
        Log.e("lzc",result2);
    }

    private void initDebugEnvironment(boolean connectable, boolean debuggable, String url) {
        Uri uri = Uri.parse(url);
        if (!uri.getQueryParameterNames().contains("_wx_devtool")) {
            return;
        }
        WXEnvironment.sDebugServerConnectable = connectable;
        WXEnvironment.sRemoteDebugMode = debuggable;
        WXEnvironment.sRemoteDebugProxyUrl = uri.getQueryParameter("_wx_devtool");
    }

    @Override
    public void configAdapter() {
        // 开发阶段可以禁用js bundle缓存
        CmlEnvironment.CML_ALLOW_BUNDLE_CACHE = true;
        // Debug开关
        CmlEnvironment.CML_DEBUG = true;
        // 开发阶段手动降级测试
//        CmlEnvironment.CML_DEGRADE = false;

        // 注册降级Adapter
        CmlEnvironment.setDegradeAdapter(new CmlDegradeDefault());
//        CmlEnvironment.setToastAdapter(xxx);
//        CmlEnvironment.setLoggerAdapter(xxx);
//        CmlEnvironment.setDialogAdapter(xxx);
//        CmlEnvironment.setNavigatorAdapter(xxx);
//        CmlEnvironment.setStatisticsAdapter(xxx);
//        CmlEnvironment.setImageLoaderAdapter(xxx);

    }

    @Override
    public void registerModule() {
        CmlEngine.getInstance().registerModule(ModuleDemo.class);
    }
}
