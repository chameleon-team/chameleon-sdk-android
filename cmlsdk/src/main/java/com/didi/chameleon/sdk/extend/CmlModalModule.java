package com.didi.chameleon.sdk.extend;

import android.content.Context;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.adapter.modal.ICmlDialogAdapter;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlCallbackSimple;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlModule;
import com.didi.chameleon.sdk.module.CmlParam;

@CmlModule(alias = "modal")
public class CmlModalModule {

    private static final String MESSAGE = "message";
    private static final String DURATION = "duration";
    private static final String CONFIRM_TITLE = "confirmTitle";
    private static final String CANCEL_TITLE = "cancelTitle";


    @CmlMethod(alias = "showToast")
    public void toast(Context context, @CmlParam(name = MESSAGE) String msg,
                      @CmlParam(name = DURATION, admin = "2000") int duration) {
        CmlEnvironment.getModalTip().showToast(context, msg, duration);
    }

    @CmlMethod(alias = "alert")
    public void alert(Context context, @CmlParam(name = MESSAGE) String msg,
                      @CmlParam(name = CONFIRM_TITLE) String ok, final CmlCallbackSimple callback) {
        CmlEnvironment.getModalTip().showAlert(context, msg, ok, new ICmlDialogAdapter.CmlTapListener() {
            @Override
            public void onTap() {
                callback.onSuccess();
            }
        });
    }

    @CmlMethod(alias = "confirm")
    public void confirm(Context context, @CmlParam(name = MESSAGE) String msg,
                        final @CmlParam(name = CONFIRM_TITLE) String ok, final @CmlParam(name = CANCEL_TITLE) String cancel,
                        final CmlCallback<String> callback) {
        CmlEnvironment.getModalTip().showConfirm(context, msg, ok, cancel, new ICmlDialogAdapter.CmlTapListener() {
            @Override
            public void onTap() {
                callback.onCallback(ok);
            }
        }, new ICmlDialogAdapter.CmlTapListener() {
            @Override
            public void onTap() {
                callback.onCallback(cancel);
            }
        });
    }

}
