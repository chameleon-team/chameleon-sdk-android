package com.didi.chameleon.sdk.utils;

import android.content.Context;
import android.content.res.Resources;

public class CmlViewUtil {

    private static int mScreenWidth;
    private static int mScreenHeight;
    private static float mDensity;

    public static float getDensity(Context ctx) {
        if (ctx != null) {
            Resources res = ctx.getResources();
            mDensity = res.getDisplayMetrics().density;
        }
        return mDensity;
    }

    public static int getScreenWidth(Context ctx) {
        if (ctx != null) {
            Resources res = ctx.getResources();
            mScreenWidth = res.getDisplayMetrics().widthPixels;
        }
        return mScreenWidth;
    }

    public static int getScreenHeight(Context cxt) {
        if (cxt != null) {
            Resources res = cxt.getResources();
            mScreenHeight = res.getDisplayMetrics().heightPixels;
        }
        return mScreenHeight;
    }

}
