package com.didi.chameleon.sdk.adapter;

import android.widget.ImageView;

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
    void setImage(String url, ImageView view);

}
