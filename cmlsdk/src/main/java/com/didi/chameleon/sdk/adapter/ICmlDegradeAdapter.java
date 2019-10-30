package com.didi.chameleon.sdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.didi.chameleon.sdk.CmlConstant;

import java.util.HashMap;

/**
 * 降级 Adapter，当 cml weex渲染页面发生异常，会触发回调
 * Created by youzicong on 2018/10/18
 */
public interface ICmlDegradeAdapter {
    /**
     * 获取降级展示 View
     *
     * @param degradeCode 降级原因:<br>
     *                    {@link CmlConstant#FAILED_TYPE_RESOLVE},<br>
     *                    {@link CmlConstant#FAILED_TYPE_RUNTIME},<br>
     *                    {@link CmlConstant#FAILED_TYPE_DEGRADE},<br>
     *                    {@link CmlConstant#FAILED_TYPE_DOWNLOAD}
     * @return {@link DegradeViewWrapper}
     */
    DegradeViewWrapper getDegradeView(int degradeCode);

    /**
     * 降级展示 Activity
     *
     * @param activity     Context
     * @param url         降级地址，也是传入 Cml 的地址
     * @param options     降级参数，也是传入 Cml 的参数
     * @param degradeCode 降级原因:<br>
     *                    {@link CmlConstant#FAILED_TYPE_RESOLVE},<br>
     *                    {@link CmlConstant#FAILED_TYPE_RUNTIME},<br>
     *                    {@link CmlConstant#FAILED_TYPE_DEGRADE},<br>
     *                    {@link CmlConstant#FAILED_TYPE_DOWNLOAD}
     */
    void degradeActivity(@NonNull Activity activity, @NonNull String url, @Nullable HashMap<String, Object> options, int degradeCode);

    /**
     * 获取降级展示 View 包裹类，给外部周期回调。
     */
    interface DegradeViewWrapper {
        /**
         * 获取降级 View
         *
         * @param context Context
         * @return 获取 View
         */
        View getView(@NonNull Context context);

        /**
         * 销毁时，回调通知
         */
        void onDestroy();

        /**
         * @param context Context
         * @param url     降级地址，也是传入 Cml 的地址
         * @param options 降级参数，也是传入 Cml 的参数
         */
        void loadURL(@NonNull Context context, @NonNull String url, @Nullable HashMap<String, Object> options);
    }
}
