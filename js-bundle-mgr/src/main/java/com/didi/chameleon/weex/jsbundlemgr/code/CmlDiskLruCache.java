package com.didi.chameleon.weex.jsbundlemgr.code;


import android.support.annotation.Nullable;

import com.didi.chameleon.weex.jsbundlemgr.cache.DiskLruCache;
import com.didi.chameleon.weex.jsbundlemgr.utils.CmlLogUtils;
import com.didi.chameleon.weex.jsbundlemgr.utils.CmlUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * houzedong
 * create at 2018/10/9
 */
public class CmlDiskLruCache implements CmlDiskCache {

    private static final String TAG = CmlDiskLruCache.class.getSimpleName();

    public static final long DEFAULT_CACHE_SIZE = 10 * 1024 * 1024; //10Mb

    @Nullable
    private DiskLruCache diskCache;

    /**
     * @param cacheDir     本地缓存目录
     * @param maxCacheSize 指定最大缓存大小
     */
    public CmlDiskLruCache(File cacheDir, long maxCacheSize) {
        if (cacheDir == null) {
            throw new IllegalArgumentException("cacheDir is null");
        }
        if (maxCacheSize <= 0) {
            maxCacheSize = DEFAULT_CACHE_SIZE;
        }
        CmlLogUtils.d(TAG, "CmlDiskLruCache init, cacheDir=" + cacheDir.getAbsolutePath());
        try {
            diskCache = DiskLruCache.open(cacheDir, 1, 1, maxCacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件
     *
     * @param url js路径
     */
    @Override
    public synchronized File get(String url) {
        if (diskCache == null) {
            return null;
        }
        DiskLruCache.Snapshot snapshot = null;
        try {
            String key = CmlUtils.generateMd5(url);
            snapshot = diskCache.get(key);
            if (snapshot != null) {
                InputStream source = snapshot.getInputStream(0);
                if (source != null) {
                    File dir = diskCache.getDirectory();
                    File cacheFile = new File(dir.getAbsolutePath() + File.separator + key + ".0");
                    CmlLogUtils.d(TAG, "找到本地缓存....key=" + key + " path: " + cacheFile.getAbsolutePath());
                    return cacheFile;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }
        return null;
    }

    /**
     * 保存js文件
     *
     * @param url         js路径
     * @param inputStream 文件流
     */
    @Override
    public boolean save(String url, InputStream inputStream) {
        if (diskCache == null) {
            return false;
        }
        DiskLruCache.Editor editor = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        OutputStream outputStream = null;
        File file = diskCache.getDirectory();
        boolean saveSuccess = false;
        //当缓存目录被删除时，保存数据会报错，因此需要重新创建缓存目录
        if (!file.exists()) {
            CmlLogUtils.i(TAG, "缓存目录重新创建");
            if (!file.mkdirs()) {//如果创建失败直接返回
                return false;
            }
        }
        try {
            String cacheName = CmlUtils.generateMd5(url);
            editor = diskCache.edit(cacheName);
            outputStream = editor.newOutputStream(0);
            in = new BufferedInputStream(inputStream, 1024);
            out = new BufferedOutputStream(outputStream, 1024);
            int buf;
            while ((buf = in.read()) != -1) {
                out.write(buf);
            }
            out.flush();
            editor.commit();
            saveSuccess = true;
            CmlLogUtils.d(TAG, "写入磁盘缓存成功, cacheName: " + cacheName);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (editor != null) {
                    editor.abort();
                }
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                saveSuccess = false;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return saveSuccess;
    }
}
