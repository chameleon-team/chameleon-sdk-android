package com.didi.chameleon.sdk.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class CmlViewUtil {

    private static int mScreenWidth;
    private static int mScreenHeight;
    private static int mStatusBarHeight;
    private static int mNavigationHeight;
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

    public static int getScreenHeight(Context ctx) {
        if (ctx == null) {
            return mScreenHeight;
        }
        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return 0;
        }
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        } else {
            display.getMetrics(dm);
        }
        mScreenHeight = dm.heightPixels;
        return mScreenHeight;
    }

    public static int getStatusBarHeight(Context ctx) {
        if (ctx == null) {
            return mStatusBarHeight;
        }
        int height = 0;
        try {
            Class c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            height = ctx.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        mStatusBarHeight = height;
        return height;
    }

    public static int getVirtualBarHeight(Context ctx) {
        if (ctx == null) {
            return mNavigationHeight;
        }
        int height = 0;
        try {
            WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            Class c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            height = dm.heightPixels - display.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mNavigationHeight = height;
        return height;
    }

}
