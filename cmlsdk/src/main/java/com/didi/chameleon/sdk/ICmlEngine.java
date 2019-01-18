package com.didi.chameleon.sdk;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.didi.chameleon.sdk.bundle.CmlBundle;

import java.util.HashMap;
import java.util.List;

public interface ICmlEngine {
    /**
     * SDK 框架初始化入口
     *
     * @param context Context
     */
    void init(Context context);

    /**
     * 初始化预加载
     *
     * @param preloadList 预加载js bundle modle list
     */
    void initPreloadList(List<CmlBundle> preloadList);

    /**
     * 执行预加载，会根据{@link ICmlEngine#initPreloadList(List)}列表进行预加载
     */
    void performPreload();

    /**
     * 调起渲染页面
     *
     * @param activity Activity
     * @param url      js bundle 地址
     * @param options  其他参数
     */
    void launchPage(@NonNull Activity activity, String url, HashMap<String, Object> options);

    /**
     * 调起渲染页面并监听页面生成周期及回调
     *
     * @param activity       Activity
     * @param url            js bundle 地址
     * @param options        其他参数
     * @param requestCode    请求code，回调时会带回
     * @param launchCallback 回调接口，渲染页面的生命周期及操作实例都会回调给业务侧
     */
    void launchPage(@NonNull Activity activity, String url, HashMap<String, Object> options, int requestCode, ICmlLaunchCallback launchCallback);
}
