package com.didi.chameleon.sdk.adapter.thread;

public interface ICmlThreadAdapter {

    void runOnUiThread(Runnable runnable);

    void runOnWorkThread(Runnable runnable);

}
