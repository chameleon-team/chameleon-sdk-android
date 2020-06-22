package cn.zkml.care.member;

import android.app.Application;

import com.didi.chameleon.example.CmlDegradeDefault;
import com.didi.chameleon.example.ModuleDemo;
import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.ICmlConfig;

import cn.zkml.care.member.module.CmlConsoleLogModule;
import cn.zkml.care.member.module.audio.module.CmlAudioModule2;
import cn.zkml.care.member.module.record.CmlRecordModule;

public class MyApplication extends Application implements ICmlConfig {
    @Override
    public void onCreate() {
        super.onCreate();

        CmlEngine.getInstance().init(this, this);
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
        CmlEngine.getInstance().registerModule(CmlConsoleLogModule.class);
        CmlEngine.getInstance().registerModule(CmlRecordModule.class);
        CmlEngine.getInstance().registerModule(CmlAudioModule2.class);
    }
}
