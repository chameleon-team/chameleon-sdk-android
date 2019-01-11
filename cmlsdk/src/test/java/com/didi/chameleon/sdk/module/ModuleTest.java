package com.didi.chameleon.sdk.module;

import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.EngineTest;

import org.json.JSONObject;
import org.junit.Test;

public class ModuleTest {

    private static long time;

    @Test
    public void test() {

        EngineTest.init();
        monitorTime();
        CmlModuleManager.getInstance();
        monitorTime();
        CmlModuleManager.getInstance().addCmlModule(TestModule.class);
        monitorTime();

        CmlModuleManager.getInstance().invokeNative(null, "TestModule", "actionNoAn", "", null);
        monitorTime();
        CmlModuleManager.getInstance().invokeNative(null, "TestModule", "action", "", null);
        monitorTime();
        CmlModuleManager.getInstance().invokeNative(null, "TestModule", "actionPar", "12", null);
        monitorTime();
        CmlModuleManager.getInstance().invokeNative(null, "TestModule", "actionObj", "{name:aaa}", null);
        monitorTime();
        CmlModuleManager.getInstance().invokeNative(null, "TestModule", "actionObj2", "{name:aaa}", null);
        monitorTime();
        CmlModuleManager.getInstance().invokeNative(null, "TestModule", "actionCall", "{}", null);
        monitorTime();
        CmlModuleManager.getInstance().invokeWeb(null, "TestModule", "action", new TestModel("aaa"), new CmlCallback<String>(String.class) {
            @Override
            public void onCallback(@Nullable String data) {
                TestModule.easyLog("WebCallback " + String.valueOf(data));
            }
        });
        monitorTime();
        CmlModuleManager.getInstance().invokeWeb(null, "TestModule", "action", "{name:aaa}", new CmlCallback<JSONObject>(JSONObject.class) {
            @Override
            public void onCallback(@Nullable JSONObject data) {
                TestModule.easyLog("WebCallback " + String.valueOf(data));
            }
        });
        monitorTime();
        CmlModuleManager.getInstance().invokeWeb(null, "TestModule", "action", "{name:aaa}", new CmlCallback<TestModel>(TestModel.class) {
            @Override
            public void onCallback(@Nullable TestModel data) {
                TestModule.easyLog("WebCallback " + String.valueOf(data));
            }
        });
        monitorTime();
    }

    private void monitorTime() {
        if (time == 0) {
            time = System.currentTimeMillis();
        }
        System.out.println(System.currentTimeMillis() - time);
    }
}
