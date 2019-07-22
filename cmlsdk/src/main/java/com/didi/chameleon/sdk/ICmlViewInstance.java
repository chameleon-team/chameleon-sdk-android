package com.didi.chameleon.sdk;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.didi.chameleon.sdk.container.ICmlView;
import com.didi.chameleon.sdk.module.CmlCallback;

import java.util.HashMap;

/**
 * View容器的Instance需要提供的能力
 */
public interface ICmlViewInstance extends ICmlInstance, ICmlView {

    ICmlViewInstance empty = new ICmlViewInstance() {
        @Override
        public void onCreate() {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onStop() {

        }

        @Override
        public void onDestroy() {

        }

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
        public void render(String url, HashMap<String, Object> options) {

        }

        @Override
        public void invokeJsMethod(String module, String method, String args, CmlCallback callback) {

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

}
