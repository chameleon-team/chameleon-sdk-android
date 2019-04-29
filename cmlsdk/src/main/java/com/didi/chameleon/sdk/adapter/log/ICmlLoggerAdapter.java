package com.didi.chameleon.sdk.adapter.log;

public interface ICmlLoggerAdapter {

    void v(String tag, String msg);

    void d(String tag, String msg);

    void i(String tag, String msg);

    void w(String tag, String msg);

    void e(Throwable tr);

    void e(String tag, String msg);

    void e(String tag, String msg, Throwable tr);
}
