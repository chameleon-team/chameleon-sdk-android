package com.didi.chameleon.sdk.utils;

import com.didi.chameleon.sdk.CmlEnvironment;

public class CmlLogUtil {

    public static void d(String tag, String msg) {
        CmlEnvironment.getLoggerAdapter().d(tag, msg);
    }

    public static void i(String tag, String msg) {
        CmlEnvironment.getLoggerAdapter().i(tag, msg);
    }

    public static void w(String tag, String msg) {
        CmlEnvironment.getLoggerAdapter().w(tag, msg);
    }

    public static void e(String tag, String msg) {
        CmlEnvironment.getLoggerAdapter().e(tag, msg);
    }

    public static void et(Throwable throwable) {
        CmlEnvironment.getLoggerAdapter().e(throwable);
    }

}
