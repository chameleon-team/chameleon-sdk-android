package com.didi.chameleon.weex.richtextcomponent.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;

/**
 * limeihong
 * create at 2018/10/11
 */
public class CmlFontUtil {
    private static final String TYPEFACE_BARLOW = "Barlow-Medium";
    private static Typeface typeface;

    /**
     * 根据字体名称获取Typeface对象
     *
     * @param fontName 字体名字
     */
    public static Typeface getTypeface(Context context, String fontName) {
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
            typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
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
