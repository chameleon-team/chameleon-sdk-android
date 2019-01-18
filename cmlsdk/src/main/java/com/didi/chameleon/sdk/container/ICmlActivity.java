package com.didi.chameleon.sdk.container;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.AnimRes;


/**
 * 增加全屏容器特有的能力
 */
public interface ICmlActivity extends ICmlContainer {
    /**
     * 获取Activity实例
     *
     * @return
     */
    Activity getActivity();

    /**
     * 设置结果，类似对比与Activity的setResult()
     *
     * @param resultCode RequestCode
     * @param data       数据
     */
    void setPageResult(int resultCode, Intent data);

    /**
     * 覆盖动画，类比于Activity的overrideAnimation
     *
     * @param enterAnim 进入动画
     * @param exitAnim  退出动画
     */
    void overrideAnim(@AnimRes int enterAnim, @AnimRes int exitAnim);

    /**
     * 更新标题
     */
    void updateNaviTitle(String title);
}
