package com.didi.chameleon.weex.adapter;

import com.taobao.weex.common.WXJSExceptionInfo;

public class WxJsExceptionAdapter implements ICmlJSExceptionAdapter {

    private ICmlJSExceptionAdapter adapter;

    public WxJsExceptionAdapter(ICmlJSExceptionAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onJSException(WXJSExceptionInfo exception) {
        adapter.onJSException(exception);
    }
}
