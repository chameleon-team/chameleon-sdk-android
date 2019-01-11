package com.didi.chameleon.sdk.common;

import android.content.Context;

import com.didi.chameleon.sdk.adapter.modal.CmlDialogAdapter;
import com.didi.chameleon.sdk.adapter.modal.CmlDialogDefault;
import com.didi.chameleon.sdk.adapter.modal.CmlToastAdapter;
import com.didi.chameleon.sdk.adapter.modal.CmlToastDefault;

public class CmlModalTip implements CmlDialogAdapter, CmlToastAdapter {

    private CmlToastAdapter toastAdapter;
    private CmlDialogAdapter dialogAdapter;

    public CmlModalTip(CmlToastAdapter toastAdapter, CmlDialogAdapter dialogAdapter) {
        this.toastAdapter = toastAdapter;
        this.dialogAdapter = dialogAdapter;
    }

    @Override
    public void showToast(Context context, String msg, int duration) {
        if (toastAdapter == null) {
            toastAdapter = new CmlToastDefault();
        }
        toastAdapter.showToast(context, msg, duration);
    }

    @Override
    public void showAlert(Context context, String msg, String okTxt, CmlTapListener tap) {
        if (dialogAdapter == null) {
            dialogAdapter = new CmlDialogDefault();
        }
        dialogAdapter.showAlert(context, msg, okTxt, tap);
    }

    @Override
    public void showConfirm(Context context, String msg, String confirmTxt, String cancelTxt, CmlTapListener confirmListener, CmlTapListener cancelListener) {
        if (dialogAdapter == null) {
            dialogAdapter = new CmlDialogDefault();
        }
        dialogAdapter.showConfirm(context, msg, confirmTxt, cancelTxt, confirmListener, cancelListener);
    }
}
