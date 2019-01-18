package com.didi.chameleon.sdk;

/**
 * 可视页面生命周期
 * Created by youzicong on 2018/10/10
 */
public interface ICmlBaseLifecycle {
    void onCreate();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
