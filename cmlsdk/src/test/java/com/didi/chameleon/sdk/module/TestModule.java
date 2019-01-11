package com.didi.chameleon.sdk.module;

import org.json.JSONObject;

@CmlModule
public class TestModule {

    public TestModule() {
        actionPar(1);
    }

    public void actionNoAn() {
        easyLog("actionNoAn");
    }

    @CmlIgnore
    public void actionNoUse() {
        easyLog("actionNoUse");
    }

    @CmlMethod
    public void action() throws NullPointerException {
        easyLog("action");
//        throw new NullPointerException();
    }

    @CmlMethod
    public <T> void actionPar(T par) {
        easyLog("actionPar " + par);
    }

    @CmlMethod
    public void actionObj(TestModel obj) {
        easyLog("actionObj " + String.valueOf(obj));
    }

    @CmlMethod
    public void actionObj2(JSONObject obj) {
        easyLog("actionObj2 " + String.valueOf(obj));
    }

    @CmlMethod
    public void actionCall(CmlCallback<TestModel> callback) {
        easyLog("actionCall");
//        callback.onCallback(new TestModel<>("call"));
    }

    public static void easyLog(String msg) {
        System.out.println(msg);
    }


}
