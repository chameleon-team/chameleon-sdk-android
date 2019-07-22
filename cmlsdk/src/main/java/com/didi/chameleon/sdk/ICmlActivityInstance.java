package com.didi.chameleon.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.didi.chameleon.sdk.container.ICmlActivity;

/**
 * Actvity容器的Instance需要提供的能力
 */
public interface ICmlActivityInstance extends ICmlInstance, ICmlActivity {

    ICmlActivityInstance empty = new ICmlActivityInstance() {
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
        public Activity getActivity() {
            return null;
        }

        @Override
        public void setPageResult(int resultCode, Intent data) {

        }

        @Override
        public void overrideAnim(int enterAnim, int exitAnim) {

        }

        @Override
        public void updateNaviTitle(String title) {

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
