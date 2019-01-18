package com.didi.chameleon.weex.richtextcomponent.richinfo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;

import com.didi.chameleon.weex.richtextcomponent.utils.CmlFontUtil;

public class CmlRichInfoSpan extends SpannableString {

    public interface CmlSpanAction {

        void onItemClick(View widget, String tag, int index);
    }

    private CmlSpanAction action;

    public CmlRichInfoSpan(CmlRichInfo info, CmlSpanAction action) {
        super(info.message);
        this.action = action;
        addSpans(info);
    }

    private void addSpans(CmlRichInfo info) {
        if (info == null) {
            return;
        }
        boolean ret = generateFixedBean(info);
        if (ret) {
            if (!TextUtils.isEmpty(info.message) && !TextUtils.isEmpty(info.msgColor)) {
                setSpan(new ForegroundColorSpan(info.textColor), 0, info.message.length(), SPAN_INCLUSIVE_INCLUSIVE);
            }

            if (info.getBeans() == null || info.getBeans().isEmpty()) {
                return;
            }

            for (int i = 0; i < info.getBeans().size(); i++) {
                CmlRichInfo.Bean b = info.getBeans().get(i);
                if (b.startPosition >= info.message.length() || b.startPosition > b.endPosition) {
                    continue;
                }
                if (b.endPosition >= info.message.length()) {
                    b.endPosition = info.message.length() - 1;
                }

                if (b.click) {
                    setSpan(new ClickSpan(info.message.substring(b.startPosition, b.endPosition + 1), i), b.startPosition,
                            b.endPosition + 1, SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (!TextUtils.isEmpty(b.colorString)) {
                    setSpan(new ForegroundColorSpan(b.colorValue), b.startPosition,
                            b.endPosition + 1, SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if (!TextUtils.isEmpty(b.bgColorString)) {
                    setSpan(new BackgroundColorSpan(b.bgColorValue), b.startPosition,
                            b.endPosition + 1, SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (b.realSize > 0) {
                    setSpan(new AbsoluteSizeSpan(b.realSize, true), b.startPosition,
                            b.endPosition + 1, SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                int styleType = 0;
                if ("italic".equals(b.fontStyle)) {
                    styleType = Typeface.ITALIC;
                }
                if ("bold".equals(b.fontWeight)) {
                    styleType |= Typeface.BOLD;
                }
                if (styleType != 0) {
                    setSpan(new StyleSpan(styleType), b.startPosition,
                            b.endPosition + 1, SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if ("underline".equals(b.textDecoration)) {
                    setSpan(new UnderlineSpan(), b.startPosition,
                            b.endPosition + 1, SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if ("line-through".equals(b.textDecoration)) {
                    setSpan(new StrikethroughSpan(), b.startPosition,
                            b.endPosition + 1, SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                Typeface typeface = null;
                switch (b.fontName) {
                    case "sans":
                        typeface = Typeface.SANS_SERIF;
                        break;
                    case "serif":
                        typeface = Typeface.SERIF;
                        break;
                    case "monospace":
                        typeface = Typeface.MONOSPACE;
                        break;
                    default:
                        if (!TextUtils.isEmpty(b.fontName)) {
                            //从assets下获取字体文件
                            typeface = CmlFontUtil.getTypeface(b.fontName);
                        }
                }

                if (typeface != null) {
                    setSpan(new CmlCustomTypefaceSpan(typeface),
                            b.startPosition, b.endPosition + 1, SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    private boolean generateFixedBean(CmlRichInfo ret) {
        if (!TextUtils.isEmpty(ret.msgColor)) {
            try {
                if (ret.msgColor.contains("#")) {
                    ret.textColor = Color.parseColor(ret.msgColor.trim());
                } else {
                    ret.textColor = Color.parseColor("#" + ret.msgColor.trim());
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }

        if (null == ret.getBeans()) {
            return true;
        }
        for (CmlRichInfo.Bean b : ret.getBeans()) {
            if (!TextUtils.isEmpty(b.colorString)) {
                try {
                    if (b.colorString.contains("#")) {
                        b.colorValue = Color.parseColor(b.colorString.trim());
                    } else {
                        b.colorValue = Color.parseColor("#" + b.colorString.trim());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            if (!TextUtils.isEmpty(b.bgColorString)) {
                try {
                    if (b.bgColorString.contains("#")) {
                        b.bgColorValue = Color.parseColor(b.bgColorString.trim());
                    } else {
                        b.bgColorValue = Color.parseColor("#" + b.bgColorString.trim());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            if (!TextUtils.isEmpty(b.size)) {
                try {
                    b.realSize = Integer.parseInt(b.size.trim()) / 2;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private class ClickSpan extends ClickableSpan {
        private final String tag;
        private final int index;

        ClickSpan(String tag, int index) {
            this.tag = tag;
            this.index = index;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(@NonNull View widget) {
            if (action != null) {
                action.onItemClick(widget, tag, index);
            }
        }
    }
}
