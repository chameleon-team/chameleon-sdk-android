package com.didi.chameleon.weex.jsbundlemgr.utils;

import android.util.Log;

import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleEnvironment;

/**
 * 日志类默认实现
 * Created by youzicong on 2018/10/17
 */
public class CmlLogUtils {
    public static void v(String tag, String msg) {
        if (!CmlJsBundleEnvironment.DEBUG)
            return;
        Log.v(tag, msg);
    }


    public static void v(String tag, String msg, Throwable tr) {
        if (!CmlJsBundleEnvironment.DEBUG)
            return;
        Log.v(tag, msg, tr);
    }


    public static void d(String tag, String msg) {
        if (!CmlJsBundleEnvironment.DEBUG)
            return;
        Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (!CmlJsBundleEnvironment.DEBUG)
            return;
        Log.d(tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        if (!CmlJsBundleEnvironment.DEBUG)
            return;
        Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (!CmlJsBundleEnvironment.DEBUG)
            return;
        Log.i(tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        if (!CmlJsBundleEnvironment.DEBUG)
            return;
        Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (!CmlJsBundleEnvironment.DEBUG)
            return;
        Log.w(tag, msg, tr);
    }

    public static void e(String tag, String msg) {
        if (!CmlJsBundleEnvironment.DEBUG)
            return;
        Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (!CmlJsBundleEnvironment.DEBUG)
            return;
        Log.e(tag, msg, tr);
    }
}
