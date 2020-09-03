package com.didi.chameleon.weex;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.didi.chameleon.sdk.CmlConstant;
import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlActivityInstance;
import com.didi.chameleon.sdk.ICmlBaseLifecycle;
import com.didi.chameleon.sdk.ICmlLaunchCallback;
import com.didi.chameleon.sdk.adapter.ICmlDegradeAdapter;
import com.didi.chameleon.sdk.adapter.ICmlStatisticsAdapter;
import com.didi.chameleon.sdk.adapter.monitor.ICmlMonitorAdapter;
import com.didi.chameleon.sdk.container.ICmlActivity;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.sdk.utils.Util;
import com.didi.chameleon.weex.jsbundlemgr.code.CmlGetCodeStringCallback;

import org.apache.weex.IWXRenderListener;
import org.apache.weex.WXSDKInstance;
import org.apache.weex.common.WXRenderStrategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.didi.chameleon.sdk.bridge.ICmlBridgeProtocol.CML_BRIDGE_EVENT;

/**
 * @since 18/7/30
 * 主要功能:
 */
public class CmlWeexInstance implements ICmlActivityInstance, ICmlBaseLifecycle, IWXRenderListener {
    private static final String TAG = "CmlWeexInstance";
    private static final String CML_PAGE_NAME = "cml_weex";

    private static final String DEGRADE_TO_H5 = "degrade_to_h5";

    private String mInstanceId;
    private int mRequestCode;
    private CmlWXSDKInstanceWrapper mWeexInstance;
    private ICmlActivity mCmlActivity;
    private ICmlInstanceListener mInstanceListener;
    private ICmlLaunchCallback mLaunchCallback;

    private String mWXUrl;
    private String mTotalUrl;
    private long mCreateTime, mStartRenderTime;

    private HashMap<String, Object> extendsParam;

    /**
     * 是否已经渲染成功
     */
    private boolean hasRenderSuccess = false;
    private boolean renderFromLocal = false;

    public CmlWeexInstance(@NonNull ICmlActivity cmlActivity,
                           @NonNull ICmlInstanceListener listener,
                           @NonNull String instanceId,
                           int requestCode) {
        mInstanceListener = listener;
        mCmlActivity = cmlActivity;
        mRequestCode = requestCode;
        mLaunchCallback = CmlInstanceManage.getInstance().getLaunchCallback(instanceId);
    }

    /**
     * 初始化param状态，当reload页面时不需要再次初始化param
     */
    private boolean initParamSuccess = false;

    @Override
    public void onCreate() {
        mCreateTime = System.currentTimeMillis();
        hasRenderSuccess = false;
        createWXInstance();
        if (!initParamSuccess) {
            initParam();
            initParamSuccess = true;
        }
        if (null != mLaunchCallback) {
            mLaunchCallback.onCreate();
        }
    }

    private void initParam() {
    }

    @Override
    public void onResume() {
        if (mWeexInstance != null) {
            mWeexInstance.onActivityResume();
        }
        if (null != mLaunchCallback) {
            mLaunchCallback.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mWeexInstance != null) {
            mWeexInstance.onActivityPause();
        }
        if (null != mLaunchCallback) {
            mLaunchCallback.onPause();
        }
    }

    @Override
    public void onStop() {
        if (mWeexInstance != null) {
            mWeexInstance.onActivityStop();
        }
        if (null != mLaunchCallback) {
            mLaunchCallback.onStop();
        }
    }

    @Override
    public void onDestroy() {
        destroyWeexInstance();
        if (null != mLaunchCallback) {
            mLaunchCallback.onDestroy();
        }
    }

    public boolean onBackPress() {
        if (mWeexInstance != null) {
            CmlLogUtil.i(TAG, "back pressed, weex code should process back event and close current page.");
            CmlEngine.getInstance().callToJs(this, "cml", "onBackPressed", null, null);
            return true;
        }
        return false;
    }

    @Override
    public void onResult(int resultCode, String result) {
        if (null != mLaunchCallback) {
            mLaunchCallback.onResult(this, mRequestCode, resultCode, result);
        }
    }

    /**
     * 根据传入的 url 渲染页面
     *
     * @param url          传入的 url ,例如：http://xxx.xxx.xxx?cml_addr=xxx.xxx.xxx/xxx.js
     * @param extendsParam 需要传入给js 的参数
     */
    public void renderByUrl(@NonNull final String url, final HashMap<String, Object> extendsParam) {
        mTotalUrl = url;
        mWXUrl = Util.parseCmlUrl(url);
        this.extendsParam = extendsParam;
        if (CmlEnvironment.CML_DEGRADE) {
            degradeToH5(CmlConstant.FAILED_TYPE_DEGRADE);
            return;
        }

        if (url.startsWith("file://") && (CmlEnvironment.CML_DEBUG || CmlEnvironment.CML_ALLOW_LOAD_FROM_FILE)) {
            HashMap<String, Object> options = structOptionsParams(url, extendsParam);
            mWeexInstance.renderByUrl(CML_PAGE_NAME, mWXUrl, options, null, WXRenderStrategy.APPEND_ASYNC);
            return;
        }

        mWeexInstance.addUserTrackParameter(CmlConstant.WEEX_INSTANCE_URL, mTotalUrl);
        CmlWeexEngine.getInstance().performGetCode(mWXUrl, new CmlGetCodeStringCallback() {
            @Override
            public void onSuccess(String template, boolean fromLocal) {
                renderFromLocal = fromLocal;
                // 代码为空weex不会报错，需要主动降级
                if (TextUtils.isEmpty(template)) {
                    reportError(CmlConstant.FAILED_TYPE_DOWNLOAD, "code is null");
                    degradeToH5(CmlConstant.FAILED_TYPE_DOWNLOAD);
                } else {
                    HashMap<String, Object> options = structOptionsParams(url, extendsParam);
                    render(template, options);
                }
            }

            @Override
            public void onFailed(String errMsg) {
                reportError(CmlConstant.FAILED_TYPE_DOWNLOAD, errMsg);
                degradeToH5(CmlConstant.FAILED_TYPE_DOWNLOAD);
            }
        });
    }

    public static HashMap<String, Object> structOptionsParams(String url, HashMap<String, Object> extendsParam) {
        HashMap<String, Object> options = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        if (extendsParam != null) {
            params.putAll(extendsParam);
        }
        params.put(CmlEnvironment.CML_QUERY_SDK, CmlEnvironment.VERSION);
        params.put(CmlEnvironment.CML_QUERY_URL, url);
        options.put(CmlConstant.WEEX_OPTIONS_KEY, params);
        HashMap<String, Object> parse = parseUrl(url);
        if (parse != null) {
            params.putAll(parse);
        }
        //适配weex的自定内容
        options.put("bundleUrl", Util.parseCmlUrl(url));
        return options;
    }


    /**
     * 重新加载当前页面
     */
    @Override
    public void reload(String url) {
        onDestroy();
        onCreate();
        if (!TextUtils.isEmpty(url)) {
            renderByUrl(url, extendsParam);
        } else if (!TextUtils.isEmpty(mTotalUrl)) {
            renderByUrl(mTotalUrl, extendsParam);
        }
    }

    /**
     * 获取url中的参数
     */
    @Nullable
    private static HashMap<String, Object> parseUrl(@NonNull String url) {
        Uri uri = Uri.parse(Uri.decode(url));
        Set<String> names = uri.getQueryParameterNames();
        if (names == null || names.size() == 0) {
            return null;
        }
        HashMap<String, Object> paramsMaps = new HashMap<>();
        for (Iterator<String> it = names.iterator(); it.hasNext(); ) {
            String key = it.next();
            paramsMaps.put(key, uri.getQueryParameter(key));
        }
        return paramsMaps;
    }


    /**
     * @return 获取当前 Instance 的 url
     */
    public String getTotalUrl() {
        return mTotalUrl;
    }

    /**
     * 使用 weex 渲染页面
     *
     * @param template js 代码
     * @param options  参数
     */
    private void render(String template, Map<String, Object> options) {

        CmlEnvironment.getMonitorAdapter().onTrace(this,
                ICmlMonitorAdapter.TRACE_RENDER_LOAD,
                Collections.singletonMap(ICmlMonitorAdapter.PARAM_LOAD_SOURCE, (Object) (renderFromLocal ? 1 : 0)));

        mStartRenderTime = System.currentTimeMillis();
        mWeexInstance.render(CML_PAGE_NAME, template, options, null, WXRenderStrategy.APPEND_ASYNC);
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        if (mInstanceListener != null) {
            mInstanceListener.onViewCreate(view);
        }
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        hasRenderSuccess = true;
        onRenderSuccess();
        long loadDuration = System.currentTimeMillis() - mCreateTime;
        long renderDuration = System.currentTimeMillis() - mStartRenderTime;

        if (CmlEnvironment.CML_OUTPUT_STATISTICS && CmlEnvironment.getStatisticsAdapter() != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("duration", renderDuration);
            map.put("link", mTotalUrl);
            CmlEnvironment.getStatisticsAdapter().event("cml_weex_container_render_time", map);
            HashMap<String, Object> map1 = new HashMap<>();
            map.put("duration", loadDuration);
            map.put("link", mTotalUrl);
            CmlEnvironment.getStatisticsAdapter().event("cml_weex_container_load_time", map1);
        }

        Map<String, Object> monitorMap = new HashMap<>();
        monitorMap.put(ICmlMonitorAdapter.PARAM_RENDER_TIME, renderDuration);
        monitorMap.put(ICmlMonitorAdapter.PARAM_LOAD_SOURCE, renderFromLocal ? 1 : 0);
        CmlEnvironment.getMonitorAdapter().onTrace(this, ICmlMonitorAdapter.TRACE_RENDER_SUCCESS, monitorMap);

        CmlLogUtil.d(TAG, "onRenderSuccess, render_time : " + renderDuration + "   load_time : " + loadDuration);
    }

    /**
     * render渲染成功，调用回调
     */
    private void onRenderSuccess() {
        if (mInstanceListener != null) {
            mInstanceListener.onRenderSuccess();
        }
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
    }


    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        CmlLogUtil.e(TAG, "onException msg = " + msg);

        Map<String, Object> monitorMap = new HashMap<>();
        monitorMap.put(ICmlMonitorAdapter.PARAM_RENDER_TIME, System.currentTimeMillis() - mStartRenderTime);
        monitorMap.put(ICmlMonitorAdapter.PARAM_LOAD_SOURCE, renderFromLocal ? 1 : 0);
        CmlEnvironment.getMonitorAdapter().onTrace(this, ICmlMonitorAdapter.TRACE_RENDER_FAIL, monitorMap);

        if (CmlEnvironment.CML_DEBUG) {
            showDebugInfo(msg);
        } else {
            if (!hasRenderSuccess) {
                degradeToH5(CmlConstant.FAILED_TYPE_RESOLVE);
                reportError(CmlConstant.FAILED_TYPE_RESOLVE, msg);
            } else {
                reportError(CmlConstant.FAILED_TYPE_RUNTIME, msg);
            }
        }

    }

    @Override
    public void updateNaviTitle(String title) {
        // do nothing, web only need to update title
    }

    /**
     * 显示错误信息对话框
     *
     * @param info 信息
     */
    private void showDebugInfo(String info) {
        TextView view = new TextView(mCmlActivity.getActivity());
        view.setText(info);
        view.setTextIsSelectable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(mCmlActivity.getActivity());
        AlertDialog dialog;
        builder.setView(view);
        builder.setTitle("发生错误了！");
        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    /**
     * 输出错误信息, {@link ICmlStatisticsAdapter} 来接收
     *
     * @param type     错误类型
     * @param errorMsg 错误信息
     */
    private void reportError(int type, String errorMsg) {
        if (CmlEnvironment.CML_OUTPUT_STATISTICS && CmlEnvironment.getStatisticsAdapter() != null) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("type", type);
            map.put("link", mTotalUrl);
            map.put("info", errorMsg);
            CmlEnvironment.getStatisticsAdapter().event("cml_weex_container_exception", map);
        }
    }

    /**
     * 降级到{@link ICmlDegradeAdapter} 实现页面
     */
    @Override
    public void degradeToH5(final int degradeCode) {
        if (mInstanceListener != null) {
            CmlEnvironment.getThreadCenter().postMain(new Runnable() {
                @Override
                public void run() {
                    Uri degradeUri = Uri.parse(mTotalUrl);
                    String degradeToH5 = degradeUri.getQueryParameter(DEGRADE_TO_H5);
                    // 原来的uri里不包含降级参数，则添加
                    if (null == degradeToH5) {
                        degradeUri = degradeUri.buildUpon().appendQueryParameter(DEGRADE_TO_H5, "1").build();
                    }
                    mInstanceListener.onDegradeToH5(degradeUri.toString(), degradeCode);
                }
            });
        }
    }

    private void createWXInstance() {
        destroyWeexInstance();
        mWeexInstance = new CmlWXSDKInstanceWrapper(mCmlActivity.getActivity());
        mWeexInstance.setCmlInstance(this);
        mWeexInstance.registerRenderListener(this);
        mWeexInstance.onActivityCreate();

        // 注册到框架里
        mInstanceId = mWeexInstance.getInstanceId();
        CmlInstanceManage.getInstance().addInstance(this);
    }

    private void destroyWeexInstance() {
        CmlInstanceManage.getInstance().removeInstance(mInstanceId);
        if (mWeexInstance != null) {
            mWeexInstance.registerRenderListener(null);
            mWeexInstance.onActivityDestroy();
            mWeexInstance.destroy();
            mWeexInstance = null;
        }
    }


    @Override
    public Context getContext() {
        return mCmlActivity.getContext();
    }

    @Nullable
    @Override
    public View getObjectView() {
        return mCmlActivity.getObjectView();
    }


    @Override
    public Activity getActivity() {
        return mCmlActivity.getActivity();
    }

    @Override
    public boolean isActivity() {
        return mCmlActivity.isActivity();
    }

    @Override
    public boolean isView() {
        return mCmlActivity.isView();
    }

    @Override
    public boolean isInDialog() {
        return mCmlActivity.isInDialog();
    }

    @Override
    public boolean isValid() {
        return mCmlActivity.isValid();
    }

    @Override
    public void finishSelf() {
        mCmlActivity.finishSelf();
    }

    @Override
    public void setPageResult(int resultCode, Intent data) {
        // do nothing
    }

    @Override
    public void overrideAnim(int enterAnim, int exitAnim) {
        // do nothing
    }

    @Override
    public String getInstanceId() {
        return mWeexInstance.getInstanceId();
    }

    @Nullable
    @Override
    public String getTargetURL() {
        return mTotalUrl;
    }

    @Nullable
    @Override
    public String getCurrentURL() {
        return mWXUrl;
    }

    @Override
    public void nativeToJs(@NonNull String protocol) {
        if (mWeexInstance == null || TextUtils.isEmpty(protocol)) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("protocol", protocol);
        mWeexInstance.fireGlobalEventCallback(CML_BRIDGE_EVENT, params);
    }

    /**
     * 构造包装类，封装更多参数
     */
    public class CmlWXSDKInstanceWrapper extends WXSDKInstance {
        CmlWeexInstance cmlInstance;

        CmlWXSDKInstanceWrapper(Context context) {
            super(context);
        }

        void setCmlInstance(CmlWeexInstance cmlInstance) {
            this.cmlInstance = cmlInstance;
        }
    }

    /**
     * @since 18/7/30
     * 主要功能:
     */
    public interface ICmlInstanceListener {
        /**
         * 降级到h5.需要容器自己实现
         */
        void onDegradeToH5(String url, int degradeCode);

        /**
         * @param view Weex 完成Bundle解析，已经生成View
         */
        void onViewCreate(View view);

        /**
         * 渲染成功
         */
        void onRenderSuccess();
    }
}
