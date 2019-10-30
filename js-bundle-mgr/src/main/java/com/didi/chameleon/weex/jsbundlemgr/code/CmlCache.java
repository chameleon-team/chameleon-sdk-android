package com.didi.chameleon.weex.jsbundlemgr.code;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.didi.chameleon.weex.jsbundlemgr.utils.CmlFileUtils;

import java.io.File;
import java.io.InputStream;

/**

 * @since 2018/9/10
 */

public class CmlCache {

    private CmlDiskLruCache cache;

    public CmlCache(Context context, long maxCacheSize, String dirName) {
        File cacheDir = CmlFileUtils.getDiskCacheDir(context, dirName);

        if (maxCacheSize <= 0) {
            maxCacheSize = CmlDiskLruCache.DEFAULT_CACHE_SIZE;
        }
        cache = new CmlDiskLruCache(cacheDir, maxCacheSize);
    }

    public boolean isFileExist(@NonNull String url) {
        File cacheFile = cache.get(url);
        return cacheFile != null && cacheFile.exists();
    }

    @Nullable
    public String getFilePath(@NonNull String url) {
        File cacheFile = cache.get(url);
        if (cacheFile != null && cacheFile.exists()) {
            return cacheFile.getAbsolutePath();
        }
        return null;
    }

    public boolean save(String url, InputStream stream) {
       return cache.save(url, stream);
    }

}
