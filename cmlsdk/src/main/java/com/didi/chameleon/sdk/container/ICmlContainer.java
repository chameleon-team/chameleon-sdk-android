package com.didi.chameleon.sdk.container;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

/**

 * @since 2018/6/15.
 * <p>
 * 用于做复杂动态容器的代表，如WebView|Activity、Weex、RN
 */
public interface ICmlContainer {
    /**
     * 获取上下文
     *
     * @return {@link Context}
     */
    Context getContext();

    /**
     * 获取当前目标view
     */
    @Nullable
    View getObjectView();

    /**
     * 当前容器是否为Activity
     *
     * @return true则为 {@link android.app.Activity}
     */
    boolean isActivity();

    /**
     * 当前容器是否只是一个View
     *
     * @return true则为 {@link android.view.View}
     */
    boolean isView();

    /**
     * 容器结束自身的能力
     */
    void finishSelf();

    /**
     * 当前容器是否在一个Dialog中
     *
     * @return true则在Dialog中作为容器
     */
    boolean isInDialog();

    /**
     * 当前容器是否有效，对于Activity来说是否在onCreate~onDestroy之间，View是否attach的
     *
     * @return true有效
     */
    boolean isValid();
}
