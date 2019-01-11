package com.didi.chameleon.sdk.adapter.navigator;

import android.content.Context;

/**
 * 路由接口实现,URI scheme 不等于空，不等于 http，不等于 https 交给 ICmlNavigatorAdapter 处理。
 * Created by youzicong on 2018/10/11
 */
public interface ICmlNavigatorAdapter {
    String KEY = "navigator";

    /**
     * @param context Context
     * @param url     跳转地址 url
     * @return 返回 true 代表处理，返回 false，继续交给{@link android.content.Intent#ACTION_VIEW}处理
     */
    boolean navigator(Context context, String url);
}
