package com.didi.chameleon.weex.jsbundlemgr.code;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleConstant;
import com.didi.chameleon.weex.jsbundlemgr.net.CmlRequest;
import com.didi.chameleon.weex.jsbundlemgr.net.CmlResponse;
import com.didi.chameleon.weex.jsbundlemgr.utils.CmlCodeUtils;
import com.didi.chameleon.weex.jsbundlemgr.utils.CmlLogUtils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 2018/9/10
 * 网络获取代码方式的实现
 */

public class CmlGetterNetImpl implements ICmlCodeGetter {

    private CmlCache mPreloadCache;
    private CmlCache mCommonCache;
    protected CmlFileDownloader mDownLoader;
    private final Map<String, List<CmlGetCodeCallback>> callbackMaps = new HashMap<>();


    public CmlGetterNetImpl() {
        mDownLoader = new CmlFileDownloader();
    }

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
     * 对应url的代码块是否存在,默认认为网络中都能获取
     */
    @Override
    public boolean isContainsCode(String url) {
        return true;
    }

    /**
     * 获取对应url的代码块，如果CmlGetCodeCallback为空，则认为是预加载
     */
    @Override
    public void getCode(final String url, @Nullable CmlGetCodeCallback callback) {
        if (!storeCallbackNewRequest(url, callback)) {
            return;
        }
        mDownLoader.startDownload(new CmlRequest(url), new CmlFileDownloader.FileDownloaderListener() {

            @Override
            public void onSuccess(CmlResponse response, String template) {
                CmlLogUtils.i(CmlJsBundleConstant.TAG, "download success");
                handleDownLoadResult(response, url, template);
            }

            @Override
            public void onFailed(String errorMsg) {
                CmlLogUtils.e(CmlJsBundleConstant.TAG, "download failed, url = " + url + ",errorMsg = " + errorMsg);
                handleDownLoadResult(null, url, null);
            }
        });
    }

    protected boolean storeCallbackNewRequest(String url, @Nullable CmlGetCodeCallback callback) {
        synchronized (callbackMaps) {
            if (callbackMaps.containsKey(url)) {
                // 如果已经有同样链接的正在下载的任务，直接复用这个任务，不再新开
                List<CmlGetCodeCallback> list = callbackMaps.get(url);
                if (list == null) {
                    list = new ArrayList<>();
                    callbackMaps.put(url, list);
                }
                list.add(callback);
                return false;
            } else if (callback != null) {
                List<CmlGetCodeCallback> list = new ArrayList<>();
                list.add(callback);
                callbackMaps.put(url, list);
            } else {
                callbackMaps.put(url, null);
            }
            return true;
        }
    }

    /**
     * 处理下载结果。如果下载失败，返回错误回调。如果下载成功，先回调返回数据，再进行缓存
     */
    protected void handleDownLoadResult(CmlResponse response, String url, String template) {
        boolean isPreload = false;
        if (callbackMaps.get(url) == null) {
            isPreload = true;
        }
        if (TextUtils.isEmpty(template)) {
            // 下载失败了
            List<CmlGetCodeCallback> callbacks;
            synchronized (callbackMaps) {
                callbacks = callbackMaps.remove(url);
            }
            if (callbacks != null && callbacks.size() > 0) {
                for (CmlGetCodeCallback callback : callbacks) {
                    callback.onFailed("download failed, url = " + url);
                }
            }
            if (isPreload) {
                CmlLogUtils.e(CmlJsBundleConstant.TAG, "预加载文件" + url + "下载失败");
            } else {
                CmlLogUtils.e(CmlJsBundleConstant.TAG, "文件" + url + "下载失败");

            }
            return;
        }
        Map<String, String> codeMaps = CmlCodeUtils.parseCode(template);
        if (codeMaps == null || codeMaps.size() == 0) {
            // 说明是单个链接，不是合并链接jsBundle
            codeMaps = new HashMap<>();
            codeMaps.put(url, template);
        }
        List<CmlGetCodeCallback> callbacks;
        synchronized (callbackMaps) {
            callbacks = callbackMaps.remove(url);
        }
        if (callbacks != null && callbacks.size() > 0) {
            for (CmlGetCodeCallback callback : callbacks) {
                callback.onSuccess(codeMaps);
            }
        }
        // 进行缓存
        CmlCache cache = isPreload ? mPreloadCache : mCommonCache;
        for (String key : codeMaps.keySet()) {
            boolean success = cache.save(key, new ByteArrayInputStream(codeMaps.get(key).getBytes()));
            if (!success) {//保存本地文件失败
                if (isPreload) {
                    CmlLogUtils.e(CmlJsBundleConstant.TAG, "预加载文件" + url + "保存本地失败");
                } else {
                    CmlLogUtils.e(CmlJsBundleConstant.TAG, "文件" + url + "保存本地失败");

                }
            }
        }
    }
}
