package com.didi.chameleon.weex.jsbundlemgr;

import android.content.Context;

import com.didi.chameleon.sdk.bundle.CmlBundle;
import com.didi.chameleon.weex.jsbundlemgr.code.CmlGetCodeStringCallback;

import java.util.List;

/**
 * limeihong
 * create at 2018/10/17
 */
public interface CmlJsBundleManager {
    /**
     * 初始化配置
     *
     * @param cmlJsBundleMgrConfig 预加载的相关配置
     */
    void initConfig(Context context, CmlJsBundleMgrConfig cmlJsBundleMgrConfig);

    /**
     * 设置预加载列表
     */
    void setPreloadList(List<CmlBundle> preloadList);

    /**
     * 开始预加载
     */
    void startPreload();

    /**
     * 获取需要解析的js
     *
     * @param url                      js的路径
     * @param cmlGetCodeStringCallback 回调接口
     */
    void getWXTemplate(String url, CmlGetCodeStringCallback cmlGetCodeStringCallback);
}
