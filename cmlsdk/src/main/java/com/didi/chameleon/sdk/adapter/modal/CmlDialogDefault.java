package com.didi.chameleon.sdk.adapter.modal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

public class CmlDialogDefault implements ICmlDialogAdapter {

    private static final String OK = "OK";
    private static final String CANCEL = "Cancel";

    private Dialog activeDialog;

    public CmlDialogDefault() {
        CmlInstanceManage.getInstance().registerListener(new CmlInstanceManage.CmlInstanceChangeListener() {
            @Override
            public void onAddInstance(String instanceId) {

            }

            @Override
            public void onRemoveInstance(String instanceId) {
                destroy();
            }
        });
    }

    @Override
    public void showAlert(Context context, String message, String okTxt, final CmlTapListener listener) {
        if (TextUtils.isEmpty(message)) {
            message = "";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);

        final String okTitle_f = TextUtils.isEmpty(okTxt) ? OK : okTxt;
        builder.setPositiveButton(okTitle_f, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onTap();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        tracking(alertDialog);
    }

    @Override
    public void showConfirm(Context context, String message, String confirmTxt, String cancelTxt,
                            final CmlTapListener confirmListener, final CmlTapListener cancelListener) {
        if (TextUtils.isEmpty(message)) {
            message = "";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);

        final String okTitleFinal = TextUtils.isEmpty(confirmTxt) ? OK : confirmTxt;
        final String cancelTitleFinal = TextUtils.isEmpty(cancelTxt) ? CANCEL : cancelTxt;

        builder.setPositiveButton(okTitleFinal, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (confirmListener != null) {
                    confirmListener.onTap();
                }
            }
        });
        builder.setNegativeButton(cancelTitleFinal, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cancelListener != null) {
                    cancelListener.onTap();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        tracking(alertDialog);
    }

    private void tracking(Dialog dialog) {
        activeDialog = dialog;
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                activeDialog = null;
            }
        });
    }

    public void destroy() {
        if (activeDialog != null && activeDialog.isShowing()) {
            CmlLogUtil.w("", "Dismiss the active dialog");
            activeDialog.dismiss();
        }
    }
}
