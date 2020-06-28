package com.didi.chameleon.weex.adapter;

import android.widget.ImageView;

import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.adapter.ICmlImgLoaderAdapter;

import org.apache.weex.adapter.IWXImgLoaderAdapter;
import org.apache.weex.common.WXImageStrategy;
import org.apache.weex.dom.WXImageQuality;

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
        if (strategy == null) {
            return;
        }
        ICmlInstance instance = CmlInstanceManage.getInstance().getCmlInstance(strategy.instanceId);
        this.imgLoaderAdapter.setImage(instance, url, view);
    }
}
