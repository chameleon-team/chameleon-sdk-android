package com.didi.chameleon.weex.adapter;

import android.widget.ImageView;

import com.didi.chameleon.sdk.adapter.ICmlImgLoaderAdapter;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

/**
 * Weex 图片加载代理类
 * Created by youzicong on 2018/10/17
 */
public class WXImgLoaderAdapter implements IWXImgLoaderAdapter {
    private ICmlImgLoaderAdapter imgLoaderAdapter;

    public WXImgLoaderAdapter(ICmlImgLoaderAdapter imgLoaderAdapter) {
        this.imgLoaderAdapter = imgLoaderAdapter;
    }

    @Override
    public void setImage(String url, ImageView view, WXImageQuality quality, WXImageStrategy strategy) {
        this.imgLoaderAdapter.setImage(url, view);
    }
}
