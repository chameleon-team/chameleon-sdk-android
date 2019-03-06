package com.didi.chameleon.sdk.common;

import com.didi.chameleon.sdk.adapter.thread.CmlThreadDefault;
import com.didi.chameleon.sdk.adapter.thread.ICmlThreadAdapter;

public class CmlThreadCenter {

    private ICmlThreadAdapter adapter;

    public CmlThreadCenter(ICmlThreadAdapter adapter) {
        this.adapter = adapter;
    }

    private void tryCreateAdapter() {
        if (adapter != null) {
            return;
        }
        adapter = CmlThreadDefault.getDefault();
    }

    /**
     * 添加到线程池里执行任务
     */
    public void post(final Runnable r) {
        tryCreateAdapter();
        adapter.runOnWorkThread(new RunSafe(r));
    }

    /**
     * 添加到线程池执行任务，支持线程执行完后的主线程处理结果
     */
    public <T> void post(IORunnable<T> r) {
        tryCreateAdapter();
        adapter.runOnWorkThread(new RunSafe(new RunWrapper<>(this, r)));
    }


    /**
     * 主线程执行任务
     */
    public void postMain(Runnable r) {
        tryCreateAdapter();
        adapter.runOnUiThread(new RunSafe(r));
    }

    /**
     * 防止异常崩溃
     */
    private class RunSafe implements Runnable {

        private Runnable r;

        RunSafe(Runnable r) {
            this.r = r;
        }

        @Override
        public void run() {
            try {
                r.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class RunWrapper<T> implements Runnable {
        private IORunnable<T> r;
        private CmlThreadCenter center;

        private RunWrapper(CmlThreadCenter center, IORunnable<T> r) {
            this.r = r;
            this.center = center;
        }

        @Override
        public void run() {
            final T result = r.run();
            center.postMain(new Runnable() {
                @Override
                public void run() {
                    r.postRun(result);
                }
            });
        }
    }

    /**
     * 支持子线程任务，主线程处理任务结果的Runnable
     */
    public abstract static class IORunnable<T> {

        /**
         * 子线程执行任务
         */
        protected abstract T run();

        /**
         * 主线程返回执行结果
         */
        protected abstract void postRun(T t);
    }
}
