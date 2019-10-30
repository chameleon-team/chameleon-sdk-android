package com.didi.chameleon.weex.jsbundlemgr.code;

import android.content.Context;

import com.didi.chameleon.sdk.bundle.CmlBundle;
import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleMgrConfig;

import java.util.List;

/**

 * @since 2018/9/10
 */

public interface ICmlCodeManager {


    /**
     * 初始化CmlCodeManager
     *
     * @param context
     * @param cmlJsBundleMgrConfig
     */
    void init(Context context, CmlJsBundleMgrConfig cmlJsBundleMgrConfig);

    /**
     * 获取url的jsbundle
     */
    void getCode(String url, CmlGetCodeStringCallback callback);

    /**
     * 设置预加载列表
     */
    void setPreloadList(List<CmlBundle> preloadList);

    /**
     * 开始预加载
     */
    void startPreload();
}
