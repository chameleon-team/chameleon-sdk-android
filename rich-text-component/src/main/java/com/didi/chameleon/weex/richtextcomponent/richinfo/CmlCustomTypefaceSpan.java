package com.didi.chameleon.weex.richtextcomponent.richinfo;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * 实现功能：支持自定义字体Span
 * Created by lvxinsheng on 2018/6/21 下午6:01.
 */
public class CmlCustomTypefaceSpan extends MetricAffectingSpan {
    private Typeface tf;

    public CmlCustomTypefaceSpan(Typeface tf) {
        this.tf = tf;
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        applyCustomTypeface(p, tf);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        applyCustomTypeface(tp, tf);
    }

    private void applyCustomTypeface(TextPaint paint, Typeface tf) {
        if (tf == null) {
            return;
        }

        int oldStyle;

        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();

        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }
}
