package com.didi.chameleon.weex.jsbundlemgr.utils;

import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;

import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleConstant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**

 * @since 18/6/20
 * 主要功能:
 */

public class CmlUtils {

    /**
     * 对url进行md5加密处理
     *
     * @param url js路径
     * @return 返回对url md5加密后的字符串
     */
    public static String generateMd5(String url) {
        byte[] hash;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 判断当前线程是否在主线程
     *
     * @return true则代表在主线程，否则在非主线程
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
