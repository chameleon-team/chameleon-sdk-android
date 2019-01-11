package com.didi.chameleon.sdk;

import com.didi.chameleon.sdk.adapter.ThreadTest;

public class EngineTest {

    public static void init() {
        CmlEnvironment.setThreadAdapter(new ThreadTest());
        CmlEnvironment.setJsonAdapter(null);
    }

}
