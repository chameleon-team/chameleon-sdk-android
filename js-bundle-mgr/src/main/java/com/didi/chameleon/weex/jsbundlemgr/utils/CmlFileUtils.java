package com.didi.chameleon.weex.jsbundlemgr.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;

/**
 * limeihong
 * create at 2018/10/9
 */
public class CmlFileUtils {
    private static String EXTERNAL_CACHE_DIR = "";

    /**
     * 判断SDCard是否存在
     */
    public static boolean haveSDCard() {
        String externalState = "";
        try {
            externalState = Environment.getExternalStorageState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Environment.MEDIA_MOUNTED.equals(externalState);
    }

    /**
     * Get a usable cache directory (external if available, internal otherwise).
     *
     * @param context    The context to use
     * @param uniqueName A unique directory name to append to the cache dir
     * @return The cache dir
     */
    public static File getDiskCacheDir(@NonNull Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        final String cachePath;
        if (!TextUtils.isEmpty(getExternalCacheDir(context))) {
            cachePath = haveSDCard() ? getExternalCacheDir(context) :
                    context.getCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    private static String getExternalCacheDir(Context context) {
        if (context == null) {
            return null;
        }
        if (TextUtils.isEmpty(EXTERNAL_CACHE_DIR)) {
            if (context.getExternalCacheDir() != null) {
                try {
                    EXTERNAL_CACHE_DIR = context.getExternalCacheDir().getPath();
                } catch (Exception e) {
                    final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
                    EXTERNAL_CACHE_DIR = Environment.getExternalStorageDirectory().getPath() + cacheDir;
                }
            } else {
                // Before Froyo we need to construct the external cache dir ourselves
                final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
                EXTERNAL_CACHE_DIR = Environment.getExternalStorageDirectory().getPath() + cacheDir;
            }
        }
        return EXTERNAL_CACHE_DIR;
    }
}
