package com.didi.chameleon.weex.adapter;

import android.content.Context;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.CmlInstanceManage;
import org.apache.weex.common.WXJSExceptionInfo;

public class CmlDefaultJsExceptionAdapter implements ICmlJSExceptionAdapter {
    @Override
    public void onJSException(WXJSExceptionInfo exception) {
        if (!CmlEnvironment.CML_DEBUG) {
            return;
        }
        try {
            String msg = exception.toString();
            String instanceId = exception.getInstanceId();
            Context context = CmlInstanceManage.getInstance().getCmlInstance(instanceId).getContext();
            CmlEnvironment.getModalTip().showAlert(context, msg, "", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
