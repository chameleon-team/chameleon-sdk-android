package com.didi.chameleon.weex.jsbundlemgr.code;

/**

 * @since 2018/9/10
 */

public interface ICmlCodeGetter {

    /**
     * @param preloadCache 预加载缓存池
     * @param commonCache  正常缓存池
     */
    public void initCodeGetter(CmlCache preloadCache, CmlCache commonCache);

    /**
     * 对应url的代码块是否存在
     */
    public boolean isContainsCode(String url);

    /**
     *获取对应url的代码块，如果BtsGetCodeCallback为空，则认为是预加载
     */
    public void getCode(String url, CmlGetCodeCallback callback);

}
