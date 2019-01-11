package com.didi.chameleon.weex.jsbundlemgr.code;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.didi.chameleon.weex.jsbundlemgr.utils.CmlCodeUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

/**

 * @since 2018/9/10
 * 文件获取代码方式的实现
 */

public class CmlGetterFileImpl implements ICmlCodeGetter {
    private CmlCache mPreloadCache;
    private CmlCache mCommonCache;

    /**
     * @param preloadCache 预加载缓存池
     * @param commonCache  正常缓存池
     */
    @Override
    public void initCodeGetter(@NonNull CmlCache preloadCache, @NonNull CmlCache commonCache) {
        this.mPreloadCache = preloadCache;
        this.mCommonCache = commonCache;
    }

    /**
     * 对应url的代码块是否存在
     */
    @Override
    public boolean isContainsCode(String url) {
        return mPreloadCache.isFileExist(url) || mCommonCache.isFileExist(url);
    }


    /**
     * 获取对应url的代码块，如果CmlGetCodeCallback为空，则认为是预加载
     */
    @Override
    public void getCode(String url, CmlGetCodeCallback callback) {
        if (callback == null || TextUtils.isEmpty(url)) {
            return;
        }
        String[] urls = CmlCodeUtils.parseUrl(url);
        if (urls == null || urls.length == 0) {
            callback.onFailed("url is null!");
            return;
        }
        final HashMap<String, String> codes = new HashMap<>();
        for (String singleUrl : urls) {
            String code = getCodeFromFile(singleUrl);
            codes.put(singleUrl, code);
        }
        callback.onSuccess(codes);
    }

    private String getCodeFromFile(String url) {
        String filePath = null;
        if (mPreloadCache.isFileExist(url)) {
            filePath = mPreloadCache.getFilePath(url);
        } else {
            filePath = mCommonCache.getFilePath(url);
        }
        if (filePath == null) {
            return null;
        }
        try {
            return CmlCodeUtils.getCodeFromStream(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
