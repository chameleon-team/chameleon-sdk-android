package com.didi.chameleon.weex.jsbundlemgr.code;

import android.content.Context;
import android.support.annotation.NonNull;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.bundle.CmlBundle;
import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleConstant;
import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleEnvironment;
import com.didi.chameleon.weex.jsbundlemgr.CmlJsBundleMgrConfig;
import com.didi.chameleon.weex.jsbundlemgr.utils.CmlCodeUtils;
import com.didi.chameleon.weex.jsbundlemgr.utils.CmlLogUtils;
import com.didi.chameleon.weex.jsbundlemgr.utils.CmlNetworkUtils;
import com.didi.chameleon.weex.jsbundlemgr.utils.CmlUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by youzicong on 2018/10/9
 */
public class CmlCodeManager implements ICmlCodeManager {

    private static final String TAG = CmlCodeManager.class.getSimpleName();

    private static CmlCodeManager instance;
    private List<ICmlCodeGetter> mCodeGetterList = new ArrayList<>();
    private ICmlCodeGetter mNetGetter;
    private ICmlCodeGetter mFileGetter;
    private Context context;

    private List<CmlBundle> mPreloadList = new ArrayList<>();

    //是否初始化
    private volatile boolean isInit = false;

    public static ICmlCodeManager getInstance() {
        if (instance == null) {
            instance = new CmlCodeManager();
        }
        return instance;
    }

    private CmlCodeManager() {
    }

    @Override
    public void init(Context context, CmlJsBundleMgrConfig cmlJsBundleMgrConfig) {
        if (isInit) {
            return;
        }
        if (!CmlUtils.isMainThread()) {
            throw new RuntimeException("请在主线程初始化CmlCodeManager");
        }
        this.context = context.getApplicationContext();
        CmlCache preLoadCache = new CmlCache(context, cmlJsBundleMgrConfig.maxPreloadSize, CmlJsBundleConstant.FILE_NAME_PRELOAD);
        CmlCache commonCache = new CmlCache(context, cmlJsBundleMgrConfig.maxRuntimeSize, CmlJsBundleConstant.FILE_NAME_COMMON);
        mFileGetter = new CmlGetterFileImpl();
        if (CmlJsBundleEnvironment.CML_HTTP_304) {
            mNetGetter = new CmlGetterHttpImpl(context, mFileGetter);
            mCodeGetterList.add(mNetGetter);
        } else {
            mNetGetter = new CmlGetterNetImpl();
            mCodeGetterList.add(mFileGetter);
            mCodeGetterList.add(mNetGetter);
        }
        mFileGetter.initCodeGetter(preLoadCache, commonCache);
        mNetGetter.initCodeGetter(preLoadCache, commonCache);
        isInit = true;
    }

    /**
     * @param url jsBundle url
     *            工作流程：1、拆解url（一个jsbundle url可能由多个子jsbundle组成）
     *            ******* 2、分别查看这些子jsbundle是否有文件缓存，如果有，从文件缓存中取，如果没有，从网络中获取
     *            ******* 3、所有子jsBundle 都获取完成时，合并所有子代码为一个大的代码块，返回。
     */
    @Override
    public void getCode(final String url, @NonNull final CmlGetCodeStringCallback callback) {
        if (!isInit) {
            CmlLogUtils.e(TAG, "请先初始化CmlCodeManager");
            return;
        }
        //先拆分url
        String[] urlArray = CmlCodeUtils.parseUrl(url);
        if (urlArray == null || urlArray.length == 0) {
            CmlLogUtils.e(CmlJsBundleConstant.TAG, "url is null or url parse failed! url = " + url);
            callback.onFailed("url is null or url parse failed! url = " + url);
            return;
        }
        CmlGetCodeCallback codeCallback = new CmlGetCodeCallback() {

            private Map<String, String> storeCodes = new HashMap<>();

            @Override
            public void onSuccess(Map<String, String> codes) {
                if (codes != null) {
                    storeCodes.putAll(codes);
                }
                if (CmlCodeUtils.isCodeFull(url, storeCodes)) {
                    // 代码都获取完毕
                    CmlEnvironment.getThreadCenter().postMain(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(CmlCodeUtils.mergeCode(storeCodes));
                        }
                    });

                } else {
                    CmlEnvironment.getThreadCenter().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailed("CmlCodeUtils.isCodeFull() return false!");
                        }
                    });
                }
            }

            @Override
            public void onFailed(final String errMsg) {
                CmlEnvironment.getThreadCenter().post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailed(errMsg);
                    }
                });

            }
        };
        for (ICmlCodeGetter getter : mCodeGetterList) {
            if (!CmlJsBundleEnvironment.CML_ALLOW_CACHE && getter instanceof CmlGetterFileImpl) {
                // 支持调试时关闭文件缓存
                CmlLogUtils.d(CmlJsBundleConstant.TAG, "cml cache is close");
                continue;
            }
            List<String> canHandleUrls = new ArrayList<>();
            for (int i = 0; i < urlArray.length; i++) {
                String singleUrl = urlArray[i];
                if (singleUrl != null && getter.isContainsCode(singleUrl)) {
                    canHandleUrls.add(singleUrl);
                    urlArray[i] = null;
                }
            }
            if (canHandleUrls.size() > 0) {
                // 获取代码
                getter.getCode(CmlCodeUtils.mergeUrl(canHandleUrls), codeCallback);
            }
        }


    }

    /**
     * <h3>设置预加载列表</h3>
     * 在startPreload之前设置即可
     *
     * @param preloadList 预加载列表参数，参考{@link CmlBundle}
     */
    public void setPreloadList(List<CmlBundle> preloadList) {
        if (preloadList == null || preloadList.size() == 0) {
            CmlLogUtils.w(TAG, "请设置预加载jsbundle路径");
            return;
        }
        mPreloadList = preloadList;
    }

    /**
     * 开始预加载
     * 预加载逻辑： 1、预加载项具有优先级，达到{@link CmlBundle#PRIORITY_FORCE}级别为强预加载，即无论什么网络环境下都加载
     * 2、预加载时需要先判断本地是否有这个缓存，如果有不再预加载
     */
    @Override
    public void startPreload() {
        if (!isInit) {
            CmlLogUtils.e(TAG, "请先初始化CmlCodeManager");
            return;
        }
        PriorityQueue<CmlBundle> queue = getPreloadList();
        if (queue == null || queue.size() == 0) {
            return;
        }
        boolean isWifi = CmlNetworkUtils.getNetStatus(context) == CmlNetworkUtils.NET_WIFI;
        while (queue.peek() != null) {
            CmlBundle model = queue.poll();
            String[] urls = CmlCodeUtils.parseUrl(model.bundle);
            if (urls == null || urls.length == 0) {
                continue;
            }
            for (String url : urls) {
                if (isNeedDownLoad(model.priority, url, isWifi)) {
                    mNetGetter.getCode(model.bundle, null);
                }
            }
        }
    }

    /**
     * 获取预加载列表
     */
    private PriorityQueue<CmlBundle> getPreloadList() {
        if (mPreloadList == null || mPreloadList.size() == 0) {
            CmlLogUtils.w(TAG, "预加载列表为空");
            return null;
        }
        Collections.sort(mPreloadList);
        PriorityQueue<CmlBundle> queue = new PriorityQueue<>(mPreloadList.size());
        queue.addAll(mPreloadList);
        return queue;
    }

    /**
     * @return 根据本地是否有缓存、版本号是否更新、网络状态 判断是否需要下载
     */
    private boolean isNeedDownLoad(int priority, String url, boolean isWifi) {
        if (mFileGetter.isContainsCode(url)) {
            // 本地已经存在
            return false;
        }
        return isWifi || priority >= CmlBundle.PRIORITY_FORCE;
    }
}