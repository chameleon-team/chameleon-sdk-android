package com.didi.chameleon.weex;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.didi.chameleon.sdk.CmlConstant;
import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlViewInstance;
import com.didi.chameleon.sdk.adapter.ICmlDegradeAdapter;
import com.didi.chameleon.sdk.adapter.ICmlStatisticsAdapter;
import com.didi.chameleon.sdk.container.ICmlView;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.sdk.utils.Util;
import com.didi.chameleon.weex.jsbundlemgr.code.CmlGetCodeStringCallback;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.didi.chameleon.sdk.bridge.ICmlBridgeProtocol.CML_BRIDGE_EVENT;

/**

 * @since 18/7/30
 * 主要功能:
 */

public class CmlWeexViewInstance implements ICmlViewInstance, IWXRenderListener {
    private static final String TAG = "CmlWeexViewInstance";

    private CmlWXSDKInstanceWrapper mWeexInstance;
    private ICmlView mCmlView;
    private String mCmlUrl;
    private String mTotalUrl;
    private long mCreateTime, mStartRenderTime;

    private ICmlInstanceListener mInstanceListener;
    private String mInstanceId;

    private HashMap<String, Object> extendsParam;

    /**
     * 是否已经渲染成功
     */
    private boolean hasRenderSuccess = false;

    public CmlWeexViewInstance(@NonNull ICmlView cmlView,
                               @NonNull ICmlInstanceListener listener) {
        mInstanceListener = listener;
        mCmlView = cmlView;
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
            initParamSuccess = true;
        }
    }

    @Override
    public void onResume() {
        if (mWeexInstance != null) {
            mWeexInstance.onActivityResume();
        }
    }

    @Override
    public void onPause() {
        if (mWeexInstance != null) {
            mWeexInstance.onActivityPause();
        }
    }

    @Override
    public void onStop() {
        if (mWeexInstance != null) {
            mWeexInstance.onActivityStop();
        }
    }

    @Override
    public void onDestroy() {
        destroyWeexInstance();
    }

    @Override
    public void onResult(int resultCode, String result) {
        // do nothing
    }

    /**
     * 根据传入的 url 渲染页面
     *
     * @param url          传入的 url ,例如：http://xxx.xxx.xxx?cml_addr=xxx.xxx.xxx/xxx.js
     * @param extendsParam 需要传入给js 的参数
     */
    @Override
    public void render(@NonNull final String url, final HashMap<String, Object> extendsParam) {
        mTotalUrl = url;
        this.extendsParam = extendsParam;
        if (CmlEnvironment.CML_DEGRADE) {
            degradeToH5(CmlConstant.FAILED_TYPE_DEGRADE);
            return;
        }

        mCmlUrl = Util.parseCmlUrl(url);
        if (TextUtils.isEmpty(mCmlUrl)) {
            degradeToH5(CmlConstant.FAILED_TYPE_DOWNLOAD);
            return;
        }
        mWeexInstance.addUserTrackParameter(CmlConstant.WEEX_INSTANCE_URL, mTotalUrl);
        CmlWeexEngine.getInstance().performGetCode(mCmlUrl, new CmlGetCodeStringCallback() {
            @Override
            public void onSuccess(String template) {
                // 代码为空weex不会报错，需要主动降级
                if (TextUtils.isEmpty(template)) {
                    reportError(CmlConstant.FAILED_TYPE_DOWNLOAD, "code is null");
                    degradeToH5(CmlConstant.FAILED_TYPE_DOWNLOAD);
                } else {
                    HashMap<String, Object> options = new HashMap<>();
                    HashMap<String, Object> params = new HashMap<>();
                    if (extendsParam != null) {
                        params.putAll(extendsParam);
                    }
                    params.putAll(parseUrl(url));
                    options.put(CmlConstant.WEEX_OPTIONS_KEY, params);
                    renderView(template, options);
                }
            }

            @Override
            public void onFailed(String errMsg) {
                reportError(CmlConstant.FAILED_TYPE_DOWNLOAD, errMsg);
                degradeToH5(CmlConstant.FAILED_TYPE_DOWNLOAD);
            }
        });
    }

    /**
     * 重新加载当前页面
     */
    @Override
    public void reload(String url) {
        onDestroy();
        onCreate();
        if (!TextUtils.isEmpty(url)) {
            render(url, extendsParam);
        } else if (!TextUtils.isEmpty(mTotalUrl)) {
            render(mTotalUrl, extendsParam);
        }
    }

    /**
     * 获取url中的参数
     */
    private HashMap<String, Object> parseUrl(@NonNull String url) {
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
    private void renderView(String template, Map<String, Object> options) {
        mStartRenderTime = System.currentTimeMillis();
        mWeexInstance.render(CmlEnvironment.CML_PAGE_NAME, template, options, null, WXRenderStrategy.APPEND_ASYNC);
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
        if (CmlEnvironment.DEBUG) {
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

    /**
     * 显示错误信息对话框
     *
     * @param info 信息
     */
    private void showDebugInfo(String info) {
        TextView view = new TextView(mCmlView.getContext());
        view.setText(info);
        view.setTextIsSelectable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(mCmlView.getContext());
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
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 输出错误信息, {@link ICmlStatisticsAdapter} 来接收
     *
     * @param type     错误累心
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
    public void degradeToH5(int degradeCode) {
        if (mInstanceListener != null) {
            mInstanceListener.onDegradeToH5(mTotalUrl, degradeCode);
        }
    }

    private void createWXInstance() {
        destroyWeexInstance();
        mWeexInstance = new CmlWXSDKInstanceWrapper(mCmlView.getContext());
        mWeexInstance.setCmlInstance(this);
        mWeexInstance.registerRenderListener(this);
        mWeexInstance.onActivityCreate();

        // 注册到框架里
        mInstanceId = mWeexInstance.getInstanceId();
        CmlInstanceManage.getInstance().addViewInstance(mCmlView.getContext(), mInstanceId, this);
    }

    private void destroyWeexInstance() {
        CmlInstanceManage.getInstance().removeViewInstance(mInstanceId);
        if (mWeexInstance != null) {
            mWeexInstance.registerRenderListener(null);
            mWeexInstance.onActivityDestroy();
            mWeexInstance.destroy();
            mWeexInstance = null;
        }
    }


    @Override
    public Context getContext() {
        return mCmlView.getContext();
    }

    @Nullable
    @Override
    public View getObjectView() {
        return mCmlView.getObjectView();
    }

    @Override
    public boolean isActivity() {
        return mCmlView.isActivity();
    }

    @Override
    public boolean isView() {
        return mCmlView.isView();
    }

    @Override
    public boolean isInDialog() {
        return mCmlView.isInDialog();
    }

    @Override
    public boolean isValid() {
        return mCmlView.isValid();
    }

    @Override
    public void finishSelf() {
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
        return mCmlUrl;
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
        private CmlWeexViewInstance cmlInstance;

        CmlWXSDKInstanceWrapper(Context context) {
            super(context);
        }

        void setCmlInstance(CmlWeexViewInstance cmlInstance) {
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
