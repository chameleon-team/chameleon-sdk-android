package com.didi.chameleon.sdk.adapter.modal;

import android.content.Context;

public interface ICmlDialogAdapter {

    void showAlert(Context context, String msg, String okTxt, CmlTapListener tap);

    void showConfirm(Context context, String msg, String confirmTxt, String cancelTxt,
                     CmlTapListener confirmListener, CmlTapListener cancelListener);

    interface CmlTapListener {
        void onTap();
    }
}
