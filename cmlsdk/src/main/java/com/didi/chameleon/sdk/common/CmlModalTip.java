package com.didi.chameleon.sdk.common;

import android.content.Context;

import com.didi.chameleon.sdk.adapter.modal.CmlDialogDefault;
import com.didi.chameleon.sdk.adapter.modal.CmlProgressDefault;
import com.didi.chameleon.sdk.adapter.modal.CmlToastDefault;
import com.didi.chameleon.sdk.adapter.modal.ICmlDialogAdapter;
import com.didi.chameleon.sdk.adapter.modal.ICmlProgressAdapter;
import com.didi.chameleon.sdk.adapter.modal.ICmlToastAdapter;

public class CmlModalTip implements ICmlDialogAdapter, ICmlToastAdapter {

    private ICmlToastAdapter toastAdapter;
    private ICmlDialogAdapter dialogAdapter;
    private ICmlProgressAdapter progressAdapter;

    public CmlModalTip(ICmlToastAdapter toastAdapter, ICmlDialogAdapter dialogAdapter, ICmlProgressAdapter progressAdapter) {
        this.toastAdapter = toastAdapter;
        this.dialogAdapter = dialogAdapter;
        this.progressAdapter = progressAdapter;
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

    public void showProgress(Context context, String title, boolean mask) {
        if (progressAdapter == null) {
            progressAdapter = new CmlProgressDefault();
        }
        progressAdapter.show(context, title, mask);
    }

    public void hideProgress() {
        if (progressAdapter != null) {
            progressAdapter.dismiss();
        }
    }

}
