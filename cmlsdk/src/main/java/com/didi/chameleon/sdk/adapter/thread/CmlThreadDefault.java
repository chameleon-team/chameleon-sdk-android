package com.didi.chameleon.sdk.adapter.thread;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CmlThreadDefault implements ICmlThreadAdapter {

    public static CmlThreadDefault getDefault() {
        return new CmlThreadDefault();
    }

    private static final String THREAD_NAME_PREFIX = "CmlIOThreader_";
    private static int threadSerialNum = 0;
    protected ThreadPoolExecutor mExecutorService;
    protected Handler mainHandler;

    public CmlThreadDefault() {
        createHandler();
        createPool();
    }

    protected void createHandler() {
        mainHandler = new Handler(Looper.getMainLooper());
    }

    protected void createPool() {
        mExecutorService = new ThreadPoolExecutor(1, Integer.MAX_VALUE,
                100L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull Runnable r) {
                        String threadName = THREAD_NAME_PREFIX + (threadSerialNum++);
                        return new Thread(r, threadName);
                    }
                });
    }

    @Override
    public void runOnUiThread(Runnable runnable) {
        mainHandler.post(runnable);
    }

    @Override
    public void runOnWorkThread(Runnable runnable) {
        mExecutorService.execute(runnable);
    }

}
