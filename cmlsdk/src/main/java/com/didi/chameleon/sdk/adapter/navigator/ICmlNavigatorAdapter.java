package com.didi.chameleon.sdk.adapter.navigator;

import android.content.Context;

/**
 * 路由接口实现,URI scheme 不等于空，不等于 http，不等于 https 交给 ICmlNavigatorAdapter 处理。
 * Created by youzicong on 2018/10/11
 */
public interface ICmlNavigatorAdapter {
    /**
     * @param context Context
     * @param url     跳转地址 url
     */
    void navigator(Context context, String url);

}
