package com.didi.chameleon.weex.jsbundlemgr.code;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.didi.chameleon.weex.jsbundlemgr.net.CmlHttpClient;
import com.didi.chameleon.weex.jsbundlemgr.net.CmlRequest;
import com.didi.chameleon.weex.jsbundlemgr.net.CmlResponse;
import com.didi.chameleon.weex.jsbundlemgr.net.ICmlHttpListener;
import com.didi.chameleon.weex.jsbundlemgr.utils.CmlLogUtils;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * houzedong
 * create at 2018/10/9
 */
public class CmlFileDownloader {
    private static final String THREAD_NAME_PREFIX = "CmlFileDownloader#";

    private CmlHttpClient httpClient;

    private static int count = 1;

    private static final ThreadPoolExecutor sExecutorService = new ThreadPoolExecutor(2, 4, 100L, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<Runnable>(100),
            new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    String threadName = THREAD_NAME_PREFIX + count++;
                    CmlLogUtils.d("CmlFileDownloader", "newThread:" + threadName);
                    return new Thread(r, threadName);
                }
            }, new ThreadPoolExecutor.DiscardOldestPolicy());

    public CmlFileDownloader() {
        httpClient = new CmlHttpClient();
    }

    /**
     * 开始下载指定的url资源文件
     *
     * @param listener 下载成功后回调接口
     */
    public void startDownload(final String url, final FileDownloaderListener listener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                CmlRequest cmlRequest = new CmlRequest();
                cmlRequest.url = url;
                httpClient.execute(cmlRequest, new ICmlHttpListener() {
                    @Override
                    public void onHttpStart() {
                        CmlLogUtils.w("CmlFileDownloader", "开始下载jsbundle");
                    }

                    @Override
                    public void onHttpProgress(int currentLength, int countLength) {
                        CmlLogUtils.w("CmlFileDownloader", "currentLength," + currentLength + ",byteCount:" + countLength + ",progress" + (currentLength * 100 / countLength) + "%");
                    }

                    @Override
                    public void onHttpFinish(CmlResponse response) {
                        if (response != null) {
                            CmlLogUtils.w("CmlFileDownloader", "response：" + response.toString());
                            if (response.data != null && TextUtils.equals("200", response.statusCode)) {
                                String template = new String(response.data);
                                if (TextUtils.isEmpty(template)) {
                                    listener.onFailed(response.errorMsg);
                                } else {
                                    listener.onSuccess(template);
                                }
                            } else {
                                listener.onFailed(response.errorMsg);
                            }

                        }
                    }
                });
            }
        });
    }

    public interface FileDownloaderListener {
        /**
         * 下载成功
         *
         * @param template 下载后返回js
         */
        void onSuccess(String template);

        /**
         * 下载失败
         */
        void onFailed(String errorMsg);

    }
}
