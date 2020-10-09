package com.didi.chameleon.sdk.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.didi.chameleon.sdk.ICmlInstance;

/**
 * 图片加载实现接口
 * Created by youzicong on 2018/10/17
 */
public interface ICmlImgLoaderAdapter {
    /**
     * 设置图片
     *
     * @param url  图片地址
     * @param view 显示图片 view
     */
    void setImage(ICmlInstance instance, String url, ImageView view, @Nullable OnLoadCallback callback);

    interface OnLoadCallback {
        void onFinish(boolean result);
    }

}
