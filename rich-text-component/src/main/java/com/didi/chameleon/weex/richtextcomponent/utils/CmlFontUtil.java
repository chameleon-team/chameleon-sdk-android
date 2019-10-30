package com.didi.chameleon.weex.richtextcomponent.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;

/**
 * create at 2018/10/11
 */
public class CmlFontUtil {
    private static String typeName;
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

        String fontPath = fontPath(fontName);
        if (TextUtils.isEmpty(fontPath)) {
            return null;
        }

        if (!fontName.equals(typeName) || typeface == null) {
            typeName = fontName;
            typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
        }

        return typeface;
    }

    private static String fontPath(String fontName) {
        //后续扩展，外部添加字体
        return null;
    }

}
