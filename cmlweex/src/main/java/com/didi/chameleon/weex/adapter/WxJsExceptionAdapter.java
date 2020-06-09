package com.didi.chameleon.weex.adapter;

import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.adapter.monitor.ICmlMonitorAdapter;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

import org.apache.weex.adapter.IWXJSExceptionAdapter;
import org.apache.weex.common.WXErrorCode;
import org.apache.weex.common.WXJSExceptionInfo;

public class WxJsExceptionAdapter implements IWXJSExceptionAdapter {

    private ICmlMonitorAdapter adapter;

    public WxJsExceptionAdapter(ICmlMonitorAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onJSException(final WXJSExceptionInfo exception) {
        if (exception == null) {
            return;
        }
        CmlLogUtil.i("jsError", "errorCode:" + exception.getErrCode()
                + ", exception: " + exception.getException());

        if (WXErrorCode.WX_RENDER_ERR_NULL_KEY != exception.getErrCode()) {
            ICmlInstance instance = CmlInstanceManage.getInstance().getCmlInstance(exception.getInstanceId());
            adapter.onError(instance, new ICmlMonitorAdapter.ErrorInfo() {
                @Override
                public String getMessage() {
                    return exception.toString();
                }

                @Override
                public String getInstanceId() {
                    return exception.getInstanceId();
                }

                @Override
                public Object getError() {
                    return exception;
                }
            });
        }
    }
}
