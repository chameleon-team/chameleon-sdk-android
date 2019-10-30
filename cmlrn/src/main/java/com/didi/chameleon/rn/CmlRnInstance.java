package com.didi.chameleon.rn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.didi.chameleon.rn.bridge.CmlRnBridgePackage;
import com.didi.chameleon.sdk.CmlConstant;
import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlActivityInstance;
import com.didi.chameleon.sdk.ICmlBaseLifecycle;
import com.didi.chameleon.sdk.ICmlLaunchCallback;
import com.didi.chameleon.sdk.container.ICmlActivity;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.sdk.utils.Util;
import com.didi.chameleon.weex.jsbundlemgr.code.CmlGetCodeStringCallback;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.shell.MainReactPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

import static com.didi.chameleon.sdk.bridge.ICmlBridgeProtocol.CML_BRIDGE;

public class CmlRnInstance implements ICmlActivityInstance, ICmlBaseLifecycle, DefaultHardwareBackBtnHandler {
    private static final String TAG = "CmlRnInstance";
    private String mUrl;
    private String mTotalUrl;

    private String mInstanceId;
    private int mRequestCode;
    private ICmlActivity mCmlActivity;
    private ICmlInstanceListener mInstanceListener;
    private ICmlLaunchCallback mLaunchCallback;
    private ReactInstanceManager rnInstance;

    private HashMap<String, Object> extendsParam;

    public CmlRnInstance(@NonNull ICmlActivity cmlActivity,
                         @NonNull ICmlInstanceListener listener,
                         @NonNull String instanceId,
                         int requestCode) {
        mInstanceListener = listener;
        mCmlActivity = cmlActivity;
        mInstanceId = instanceId;
        mRequestCode = requestCode;
        mLaunchCallback = CmlInstanceManage.getInstance().getLaunchCallback(instanceId);
    }

    @Override
    public void onCreate() {
        if (null != mLaunchCallback) {
            mLaunchCallback.onCreate();
        }
    }

    @Override
    public void onResume() {
        if (rnInstance != null) {
            rnInstance.onHostResume(mCmlActivity.getActivity(), this);
        }
        if (null != mLaunchCallback) {
            mLaunchCallback.onResume();
        }
    }

    @Override
    public void onPause() {
        if (rnInstance != null) {
            rnInstance.onHostPause(mCmlActivity.getActivity());
        }
        if (null != mLaunchCallback) {
            mLaunchCallback.onPause();
        }
    }

    @Override
    public void onStop() {
        if (null != mLaunchCallback) {
            mLaunchCallback.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if (rnInstance != null) {
            rnInstance.onHostDestroy(mCmlActivity.getActivity());
            rnInstance = null;
        }

        if (mInstanceId != null) {
            CmlInstanceManage.getInstance().removeInstance(mInstanceId);
        }
        if (null != mLaunchCallback) {
            mLaunchCallback.onDestroy();
        }
    }

    @Override
    public void onResult(int resultCode, String result) {
        if (null != mLaunchCallback) {
            mLaunchCallback.onResult(this, mRequestCode, resultCode, result);
        }
    }

    private static final String RN_BUNDLE_NAME = "hello.android.bundle";

    private void createInstance(String jsBundleFile) {
        rnInstance = ReactInstanceManager.builder()
                .setApplication(mCmlActivity.getActivity().getApplication())
                .setCurrentActivity(mCmlActivity.getActivity())
//                .setBundleAssetName(RN_BUNDLE_NAME)
                .setJSMainModulePath("HelloWorld")
                .addPackages(Arrays.asList(
                        new MainReactPackage(),
                        new CmlRnBridgePackage(mInstanceId)
                ))
                .setUseDeveloperSupport(true)
//                .setJSBundleFile(jsBundleFile)
                .setInitialLifecycleState(LifecycleState.BEFORE_CREATE)
                .build();
        // 注册到框架里
        CmlInstanceManage.getInstance().addInstance(this);

        mInstanceListener.onReactInstanceCreated(rnInstance);
    }

    /**
     * 根据传入的 url 渲染页面
     *
     * @param url          传入的 url ,例如：http://xxx.xxx.xxx?cml_addr=xxx.xxx.xxx/xxx.js
     * @param extendsParam 需要传入给js 的参数
     */
    public void renderByUrl(@NonNull final String url, final HashMap<String, Object> extendsParam) {
        mTotalUrl = url;
        mUrl = Util.parseCmlUrl(url);
        this.extendsParam = extendsParam;
        if (CmlEnvironment.CML_DEGRADE) {
            degradeToH5(CmlConstant.FAILED_TYPE_DEGRADE);
            return;
        }

        CmlRnEngine.getInstance().performGetCode(mUrl, new CmlGetCodeStringCallback() {
            @Override
            public void onSuccess(String template) {
                // 代码为空weex不会报错，需要主动降级
                if (TextUtils.isEmpty(template)) {
                    reportError(CmlConstant.FAILED_TYPE_DOWNLOAD, "code is null");
                    degradeToH5(CmlConstant.FAILED_TYPE_DOWNLOAD);
                } else {
                    render(template);
                }
            }

            @Override
            public void onFailed(String errMsg) {
                reportError(CmlConstant.FAILED_TYPE_DOWNLOAD, errMsg);
                degradeToH5(CmlConstant.FAILED_TYPE_DOWNLOAD);
            }
        });
    }

    @Override
    public void updateNaviTitle(String title) {
        // do nothing, web only need to update title
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
     * @return 获取当前 Instance 的 url
     */
    public String getTotalUrl() {
        return mTotalUrl;
    }

    /**
     * 临时测试
     */
    private void render(String template) {
        final String path = "/mnt/sdcard/cml/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        final String fullPath = path + RN_BUNDLE_NAME;
        File jsBundleFile = new File(fullPath);
        if (!jsBundleFile.exists()) {
            try {
                jsBundleFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(jsBundleFile);
                PrintWriter pw = new PrintWriter(fos);
                pw.write(template);
                pw.flush();
                pw.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        CmlEnvironment.getThreadCenter().postMain(new Runnable() {
            @Override
            public void run() {
                createInstance(fullPath);
            }
        });
    }

    /**
     * 输出错误信息
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

    @Override
    public void degradeToH5(int degradeCode) {
        if (mInstanceListener != null) {
            mInstanceListener.onDegradeToH5(mTotalUrl, degradeCode);
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
        mCmlActivity.setPageResult(resultCode, data);
    }

    @Override
    public void overrideAnim(int enterAnim, int exitAnim) {
        mCmlActivity.overrideAnim(enterAnim, exitAnim);
    }

    @Nullable
    @Override
    public String getTargetURL() {
        return mTotalUrl;
    }

    @Nullable
    @Override
    public String getCurrentURL() {
        return mUrl;
    }

    @Override
    public void nativeToJs(@NonNull String protocol) {
        if (rnInstance == null) {
            return;
        }
        WritableArray params = Arguments.createArray();// RN的Array
        params.pushString(protocol);

        ReactContext reactContext = rnInstance.getCurrentReactContext();
        if (null != reactContext) {
            DeviceEventManagerModule.RCTDeviceEventEmitter emitter =
                    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
            emitter.emit(CML_BRIDGE, params);
        } else {
            CmlLogUtil.e(TAG, "nativeToJs: reactContext is null.");
        }
    }

    public String getInstanceId() {
        return mInstanceId;
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        if (mCmlActivity.getActivity() != null) {
            mCmlActivity.getActivity().onBackPressed();
        }
    }

    public interface ICmlInstanceListener {
        /**
         * 降级到h5.需要容器自己实现
         */
        void onDegradeToH5(String url, int degradeCode);

        void onReactInstanceCreated(ReactInstanceManager reactInstance);
    }
}
