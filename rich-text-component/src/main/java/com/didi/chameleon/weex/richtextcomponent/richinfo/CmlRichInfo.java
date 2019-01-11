package com.didi.chameleon.weex.richtextcomponent.richinfo;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

/**
 * 富文本数据
 *

 * @since 15/9/18.
 */

public class CmlRichInfo implements Serializable, CmlClickSpanListener {

    /**
     * 背景色，例如 #FFFFFF
     */
    @JSONField(name = "background_color")
    public String background;

    /**
     * 显示背景的框， 只对 message 生效，框的圆角, 单位是dp, 取值不带单位,比如"2.0"
     */
    @JSONField(name = "border_corner")
    public String borderCorner;
    /**
     * 框的线条颜色， 只对 message 生效， 取值规则和 color 规则一样
     */
    @JSONField(name = "border_color")
    public String borderColor;
    /**
     * 框的线条宽度， 只对 message 生效， 单位是dp, 取值不带单位,比如"0.5"
     */
    @JSONField(name = "border_width")
    public String borderWidth;

    /**
     * 当有其他富文本被系统自动截断时，是否要隐藏此富文本
     */
    @JSONField(name = "can_hide")
    public boolean canHide;

    public boolean isText() {
        return !TextUtils.isEmpty(message);
    }

    public boolean isImage() {
        return !TextUtils.isEmpty(icon);
    }

    @JSONField(name = "message")
    public String message = "";
    @JSONField(name = "color_text")
    public String msgColor;
    @JSONField(name = "font")
    public String msgFont;
    @JSONField(name = "msg_url")
    public String msgUrl;
    @JSONField(name = "icon_url")
    public String icon;
    @JSONField(name = "rich_message")
    private List<Bean> beans;

    private final RichInfoPadding defaultPadding = new RichInfoPadding(6, 3, 6, 3);
    private transient RichInfoPadding padding;

    //已经解析过的颜色，直接可用
    public transient int textColor;

    public transient boolean sizeUseDefault = true;

    /**
     * {@link Bean}设置的ClickSpan是否被点击
     */
    private transient boolean isSpanClicked = false;

    public CmlRichInfo() {

    }

    public CmlRichInfo(@NonNull String msg) {
        this.message = msg;
    }


    public List<Bean> getBeans() {
        return beans;
    }

    public void setBeans(List<Bean> beans) {
        this.beans = beans;
    }

    public void setPadding(RichInfoPadding padding) {
        if (padding != null) {
            this.padding = padding;
        }
    }

    /**
     * 判断当前富文本是不是空，
     * <p>
     * message 或者 icon 至少要有一个
     **/
    public boolean isEmpty() {
        return TextUtils.isEmpty(message) && TextUtils.isEmpty(icon);
    }

    public boolean isHaveBorder() {
        return !TextUtils.isEmpty(this.borderWidth) && !TextUtils.isEmpty(this.borderCorner) &&
                !TextUtils.isEmpty(this.borderColor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CmlRichInfo that = (CmlRichInfo) o;

        if (message != null ? !message.equals(that.message) : that.message != null) {
            return false;
        }
        if (msgColor != null ? !msgColor.equals(that.msgColor) : that.msgColor != null) {
            return false;
        }
        if (msgUrl != null ? !msgUrl.equals(that.msgUrl) : that.msgUrl != null) {
            return false;
        }
        if (icon != null ? !icon.equals(that.icon) : that.icon != null) {
            return false;
        }
        return beans != null ? beans.equals(that.beans) : that.beans == null;
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (msgColor != null ? msgColor.hashCode() : 0);
        result = 31 * result + (msgUrl != null ? msgUrl.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (beans != null ? beans.hashCode() : 0);
        return result;
    }

    private void setRichInfo(TextView textView) {
        textView.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(msgColor)) {
            msgColor = "#666666";
        }
        if (!TextUtils.isEmpty(message)) {
            message = message.replace("\\n", "\n");
        }
        textView.setText(new CmlRichInfoSpan(this));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setIncludeFontPadding(false);
        GradientDrawable drawable = new GradientDrawable();
        if (!TextUtils.isEmpty(this.background)) {
            drawable.setColor(Color.parseColor(this.background));
        } else {
            drawable.setColor(Color.TRANSPARENT);
        }
        DisplayMetrics displayMetrics = textView.getResources().getDisplayMetrics();
        if (this.isHaveBorder()) {
            if (!sizeUseDefault) {
                textView.setTextSize(10);
            }
            textView.setSingleLine();
            try {
                float borderWidth = TypedValue.applyDimension(COMPLEX_UNIT_DIP, Float.valueOf(this.borderWidth), displayMetrics);
                float borderCorner = TypedValue.applyDimension(COMPLEX_UNIT_DIP, Float.valueOf(this.borderCorner), displayMetrics);
                drawable.setStroke((int) borderWidth, Color.parseColor(this.borderColor));
                drawable.setCornerRadius(borderCorner);
            } catch (Exception ignore) {

            }
            if (null == padding) {
                padding = defaultPadding;
            }
        } else {
            if (!sizeUseDefault) {
                textView.setTextSize(TextUtils.isEmpty(background) ? 12 : 10);
            }
        }
        if (padding != null) {
            int left = (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, padding.left, displayMetrics);
            int top = (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, padding.top, displayMetrics);
            int right = (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, padding.right, displayMetrics);
            int bottom = (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, padding.bottom, displayMetrics);
            textView.setPadding(left, top, right, bottom);
        }
        textView.setBackgroundDrawable(drawable);
        if (!TextUtils.isEmpty(msgFont)) {
            int size = Integer.parseInt(msgFont) / 2;
            if (size > 0) {
                textView.setTextSize(COMPLEX_UNIT_DIP, size);
            }
        }
    }

    @Override
    public void spanClicked(View widget) {
        isSpanClicked = true;
    }

    public static class Bean implements Serializable {
        /**
         * 起始位置
         */
        @JSONField(name = "start")
        public int startPosition;
        /**
         * 终止位置
         */
        @JSONField(name = "end")
        public int endPosition;
        /**
         * 文字颜色
         */
        @JSONField(name = "color")
        public String colorString;
        @JSONField(name = "bg_color")
        public String bgColorString;
        /**
         * 文字大小，单位是像素
         */
        @JSONField(name = "font_size")
        public String size;

        /**
         * 超链接地址
         */
        @Nullable
        @JSONField(name = "link_url")
        public String link;

        /**
         * 字体名称
         */
        @JSONField(name = "font_name")
        public String fontName;

        // 已经解析过的颜色，直接可用
        transient int colorValue;
        // 已经解析过的背景色，直接可用
        transient int bgColorValue;
        // 已经解析过的大小，直接可用
        transient int realSize;

        public boolean bold;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Bean bean = (Bean) o;

            if (startPosition != bean.startPosition) {
                return false;
            }
            if (endPosition != bean.endPosition) {
                return false;
            }
            if (colorString != null ? !colorString.equals(bean.colorString) : bean.colorString != null) {
                return false;
            }
            if (bgColorString != null ? !bgColorString.equals(bean.bgColorString) : bean.bgColorString != null) {
                return false;
            }
            return size != null ? size.equals(bean.size) : bean.size == null;

        }

        @Override
        public int hashCode() {
            int result = startPosition;
            result = 31 * result + endPosition;
            result = 31 * result + (colorString != null ? colorString.hashCode() : 0);
            result = 31 * result + (bgColorString != null ? bgColorString.hashCode() : 0);
            result = 31 * result + (size != null ? size.hashCode() : 0);
            return result;
        }
    }

    public void bindView(TextView tv) {
        if (this.isEmpty()) {
            return;
        }
        if (!isHaveBorder() && !TextUtils.isEmpty(background) && !TextUtils.isEmpty(message)) {
            setPadding(new RichInfoPadding(5, 3, 5, 3));
        }
        if (tv != null) {
            setRichInfo(tv);

            if (!TextUtils.isEmpty(msgUrl)) {
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isSpanClicked) {
                            isSpanClicked = false;
                            return;
                        }
                    }
                });
            }
        }
    }


    public static class RichInfoPadding implements Serializable {

        public float left;
        public float right;
        public float top;
        public float bottom;

        public RichInfoPadding() {

        }

        public RichInfoPadding(float left, float top, float right, float bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }

        public RichInfoPadding(float padding) {
            this.left = padding;
            this.right = padding;
            this.top = padding;
            this.bottom = padding;
        }
    }
}
