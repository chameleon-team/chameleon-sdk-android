package com.didi.chameleon.sdk.adapter.modal;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.didi.chameleon.sdk.utils.CmlLogUtil;

public class CmlToastDefault implements ICmlToastAdapter {

    private Toast toast;

    @Override
    public void showToast(Context context, String message, int duration) {
        if (TextUtils.isEmpty(message)) {
            CmlLogUtil.e("", "[WXModalUIModule] toast param parse is null ");
            return;
        }

        if (duration > 3 * 1000) {
            duration = Toast.LENGTH_LONG;
        } else {
            duration = Toast.LENGTH_SHORT;
        }
        if (toast == null) {
            toast = Toast.makeText(context, message, duration);
        } else {
            toast.setDuration(duration);
            toast.setText(message);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
