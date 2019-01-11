package com.didi.chameleon.sdk.adapter;

import com.didi.chameleon.sdk.adapter.thread.CmlThreadDefault;

public class ThreadTest extends CmlThreadDefault {


    @Override
    protected void createHandler() {
    }

    @Override
    public void runOnUiThread(Runnable runnable) {
        runOnWorkThread(runnable);
    }
}
