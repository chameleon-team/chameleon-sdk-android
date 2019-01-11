package com.didi.chameleon.sdk.adapter.thread;

public interface CmlThreadAdapter {

    void runOnUiThread(Runnable runnable);

    void runOnWorkThread(Runnable runnable);

}
