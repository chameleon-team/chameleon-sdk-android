package com.didi.chameleon.weex.richtextcomponent.utils;

import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.TextView;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.utils.CmlLogUtil;


/**
 * limeihong
 * create at 2018/10/11
 */
public class CmlFontUtil {
    private static final String TYPEFACE_BARLOW = "Barlow-Medium";
    private static Typeface typeface;

    /**
     * 获取Barlow-Medium字体
     *
     * @return
     */
    public static Typeface getTypefaceBarlow() {
        return getTypeface(TYPEFACE_BARLOW);
    }

    /**
     * TextView 文本不是BtsRichInfo时设置Barlow字体
     *
     * @param tv
     */
    public static void setTvFontBarlow(TextView tv) {
        setTvFont(tv, TYPEFACE_BARLOW);
    }

    /**
     * TextView 文本不是BtsRichInfo时设置名称为fontName字体
     *
     * @param tv
     */
    public static void setTvFont(TextView tv, String fontName) {
        if (getTypeface(fontName) == null) {
            CmlLogUtil.i("typeface", "typeface == null");
            return;
        }

        tv.setTypeface(getTypeface(fontName));
    }

    /**
     * 根据字体名称获取Typeface对象
     *
     * @param fontName 字体名字
     * @return
     */
    public static Typeface getTypeface(String fontName) {
        if (TextUtils.isEmpty(fontName)) {
            return null;
        }

        String fontNameAndroid = null;

        // "dina"为富文本BtsRichInfo中font_name字段下发的，代表自定义字体Barlow-Medium，
        // 因为android与ios两端用的字体不同，此字段内容只是一个代号，需要客户端解析
        if ("dina".equals(fontName) || TYPEFACE_BARLOW.equals(fontName)) {
            fontNameAndroid = TYPEFACE_BARLOW;
        } else {
            // 未来可能添加其他字体
        }

        if (fontNameAndroid == null) {
            return null;
        }

        String fontPath = t("fonts/", fontNameAndroid, ".ttf");

        if (typeface == null) {
            typeface = Typeface.createFromAsset(CmlEngine.getInstance().getAppContext().getAssets(), fontPath);
        }

        return typeface;
    }

    /**
     * 输出参数os拼接成的String
     *
     * @param os 参数数组
     * @return 拼接结果
     */
    public static String t(Object... os) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : os) {
            stringBuilder.append(o);
        }
        return stringBuilder.toString();
    }
}
