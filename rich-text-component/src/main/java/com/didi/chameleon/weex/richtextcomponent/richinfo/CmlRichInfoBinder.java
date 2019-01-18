package com.didi.chameleon.weex.richtextcomponent.richinfo;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

public class CmlRichInfoBinder {

    public interface CmlRichInfoAction {

        void onClick(View widget, String message);

        void onSpanClick(View widget, String message, int index);

    }

    private CmlRichInfo richInfo;

    private TextView textView;
    private CmlRichInfoAction action;


    /**
     * {@link CmlRichInfo.Bean}设置的ClickSpan是否被点击
     */
    private boolean isSpanClicked = false;

    public CmlRichInfoBinder(CmlRichInfo richInfo) {
        this.richInfo = richInfo;
    }

    public void bindView(TextView tv, final CmlRichInfoAction action) {
        this.textView = tv;
        this.action = action;
        if (richInfo.isEmpty()) {
            return;
        }
        if (!richInfo.isHaveBorder()
                && !TextUtils.isEmpty(richInfo.background) && !TextUtils.isEmpty(richInfo.message)) {
            richInfo.setPadding(new CmlRichInfo.RichInfoPadding(5, 3, 5, 3));
        }
        if (tv != null) {
            setRichInfo();

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSpanClicked) {
                        isSpanClicked = false;
                    } else {
                        action.onClick(v, richInfo.message);
                    }
                }
            });
        }
    }

    private void setRichInfo() {
        textView.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(richInfo.msgColor)) {
            richInfo.msgColor = "#666666";
        }
        if (!TextUtils.isEmpty(richInfo.message)) {
            richInfo.message = richInfo.message.replace("\\n", "\n");
        }
        textView.setText(new CmlRichInfoSpan(richInfo, new CmlRichInfoSpan.CmlSpanAction() {
            @Override
            public void onItemClick(View widget, String tag, int index) {
                isSpanClicked = true;
                action.onSpanClick(widget, tag, index);
            }
        }));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setIncludeFontPadding(false);
        GradientDrawable drawable = new GradientDrawable();
        if (!TextUtils.isEmpty(richInfo.background)) {
            drawable.setColor(Color.parseColor(richInfo.background));
        } else {
            drawable.setColor(Color.TRANSPARENT);
        }
        DisplayMetrics displayMetrics = textView.getResources().getDisplayMetrics();
        if (richInfo.isHaveBorder()) {
            if (!richInfo.sizeUseDefault) {
                textView.setTextSize(10);
            }
            textView.setSingleLine();
            try {
                float borderWidth = TypedValue.applyDimension(COMPLEX_UNIT_DIP, Float.valueOf(richInfo.borderWidth), displayMetrics);
                float borderCorner = TypedValue.applyDimension(COMPLEX_UNIT_DIP, Float.valueOf(richInfo.borderCorner), displayMetrics);
                drawable.setStroke((int) borderWidth, Color.parseColor(richInfo.borderColor));
                drawable.setCornerRadius(borderCorner);
            } catch (Exception ignore) {

            }
            if (null == richInfo.padding) {
                richInfo.padding = richInfo.defaultPadding;
            }
        } else {
            if (!richInfo.sizeUseDefault) {
                textView.setTextSize(TextUtils.isEmpty(richInfo.background) ? 12 : 10);
            }
        }
        if (richInfo.padding != null) {
            int left = (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, richInfo.padding.left, displayMetrics);
            int top = (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, richInfo.padding.top, displayMetrics);
            int right = (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, richInfo.padding.right, displayMetrics);
            int bottom = (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, richInfo.padding.bottom, displayMetrics);
            textView.setPadding(left, top, right, bottom);
        }
        textView.setBackgroundDrawable(drawable);
        if (!TextUtils.isEmpty(richInfo.msgFont)) {
            int size = Integer.parseInt(richInfo.msgFont) / 2;
            if (size > 0) {
                textView.setTextSize(COMPLEX_UNIT_DIP, size);
            }
        }
    }

}
