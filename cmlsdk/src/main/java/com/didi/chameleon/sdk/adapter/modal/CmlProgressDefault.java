package com.didi.chameleon.sdk.adapter.modal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.didi.chameleon.sdk.R;
import com.didi.chameleon.sdk.utils.CmlViewUtil;

public class CmlProgressDefault implements ICmlProgressAdapter {

    private PopupWindow popupWindow;

    @Override
    public void show(Context context, String title, boolean mask) {
        if (!(context instanceof Activity)) {
            return;
        }
        Activity activity = (Activity) context;

        if (popupWindow != null && popupWindow.isShowing()) {
            dismiss();
        }

        View view = LayoutInflater.from(context).inflate(R.layout.cml_loading_dialog, null);
        if (mask) {
            FrameLayout parent = new FrameLayout(context);
            parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            parent.addView(view, new FrameLayout.LayoutParams(CmlViewUtil.dp2px(activity, 120), CmlViewUtil.dp2px(activity, 130), Gravity.CENTER));
            popupWindow = new PopupWindow(parent, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            popupWindow = new PopupWindow(view, CmlViewUtil.dp2px(activity, 120), CmlViewUtil.dp2px(activity, 130));
        }

        popupWindow.setOutsideTouchable(false);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            popupWindow.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), (Bitmap) null));
        }

        TextView titleView = popupWindow.getContentView().findViewById(R.id.cml_msg);
        if (TextUtils.isEmpty(title)) {
            titleView.setVisibility(View.GONE);
        } else {
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(title);
        }
        popupWindow.showAtLocation(activity.findViewById(Window.ID_ANDROID_CONTENT), Gravity.CENTER, 0, 0);
    }

    @Override
    public void dismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
        popupWindow = null;
    }

}
