package com.didi.chameleon.weex.richtextcomponent.richinfo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;

import com.didi.chameleon.weex.richtextcomponent.CmlRichTextComponent;
import com.didi.chameleon.weex.richtextcomponent.utils.CmlFontUtil;

/**

 * @since 15/9/18.
 * <p/>
 * 配合{@link CmlRichInfo}显示富文本文字，可直接设置给TextView
 */
public class CmlRichInfoSpan extends SpannableString {

    public CmlRichInfoSpan(@NonNull CharSequence source, CmlRichInfo info) {
        super(source);
        if (info != null && TextUtils.isEmpty(info.message)) {
            info.message = source.toString();
        }
        addSpans(info);
    }

    public CmlRichInfoSpan(CmlRichInfo info) {
        super(info.message);
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

            for (CmlRichInfo.Bean b : info.getBeans()) {
                if (b.startPosition >= info.message.length() || b.startPosition > b.endPosition) {
                    continue;
                }
                if (b.endPosition >= info.message.length()) {
                    b.endPosition = info.message.length() - 1;
                }

                if (!TextUtils.isEmpty(b.link)) {
                    setSpan(new ClickSpan(b.link, info), b.startPosition,
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
                if (b.bold) {
                    setSpan(new StyleSpan(android.graphics.Typeface.BOLD), b.startPosition,
                            b.endPosition + 1, SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (b.realSize > 0) {
                    setSpan(new AbsoluteSizeSpan(b.realSize, true), b.startPosition,
                            b.endPosition + 1, SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                if (!TextUtils.isEmpty(b.fontName)) {
                    //从assets下获取字体文件
                    setSpan(new CmlCustomTypefaceSpan(CmlFontUtil.getTypeface(b.fontName)),
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

    static class ClickSpan extends ClickableSpan {
        private final String uri;

        private CmlClickSpanListener clickSpanListener;

        ClickSpan(String url, CmlClickSpanListener clickSpanListener) {
            this.uri = url;
            this.clickSpanListener = clickSpanListener;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(@NonNull View widget) {
            if (clickSpanListener != null) {
                clickSpanListener.spanClicked(widget);
            }
            //富文本点击跳转路由
            if (CmlRichTextComponent.getOnRouterListener() != null) {
                CmlRichTextComponent.getOnRouterListener().onRoute(widget, uri);
            }
        }
    }
}
