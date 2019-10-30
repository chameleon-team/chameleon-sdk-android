package com.didi.chameleon.sdk;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.didi.chameleon.sdk.container.ICmlContainer;

/**
 * Instance 需要提供的基础能力
 */
public interface ICmlInstance extends ICmlContainer {

    ICmlInstance empty = new ICmlInstance() {
        @Override
        public String getInstanceId() {
            return null;
        }

        @Nullable
        @Override
        public String getTargetURL() {
            return null;
        }

        @Nullable
        @Override
        public String getCurrentURL() {
            return null;
        }

        @Override
        public void nativeToJs(String protocol) {

        }

        @Override
        public void reload(String url) {

        }

        @Override
        public void degradeToH5(int degradeCode) {

        }

        @Override
        public void onResult(int resultCode, String result) {

        }

        @Override
        public Context getContext() {
            return null;
        }

        @Nullable
        @Override
        public View getObjectView() {
            return null;
        }

        @Override
        public boolean isActivity() {
            return false;
        }

        @Override
        public boolean isView() {
            return false;
        }

        @Override
        public void finishSelf() {

        }

        @Override
        public boolean isInDialog() {
            return false;
        }

        @Override
        public boolean isValid() {
            return false;
        }
    };

    String getInstanceId();

    /**
     * 获取初始目标URL
     *
     * @return URL
     */
    @Nullable
    String getTargetURL();

    /**
     * 获取当前页面URL
     *
     * @return URL
     */
    @Nullable
    String getCurrentURL();

    /**
     * 实现回传数据功能
     *
     * @param protocol
     */
    void nativeToJs(String protocol);

    /**
     * 重新加载当前页面
     *
     * @param url
     */
    void reload(String url);

    /**
     * 降级到H5页面
     *
     * @param degradeCode 降级原因，参考 {@link CmlConstant}
     */
    void degradeToH5(int degradeCode);

    /**
     * 主页面调起子页面，子页面和主页面通信
     *
     * @param resultCode
     * @param result
     */
    void onResult(int resultCode, String result);
}
