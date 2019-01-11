package com.didi.chameleon.sdk.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.Keep;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Keep
public class CmlSystemUtil {
    private static final String TAG = "CmlSystemUtil";

    private static String mDeviceId = null;
    private static String strIMEI;

    /**
     * 获取手机型号
     */
    public static String getModel() {
        String temp = Build.MODEL;

        if (TextUtils.isEmpty(temp)) {
            return "";
        } else {
            return temp;
        }
    }

    /**
     * 获取sdk版本
     */
    public static String getVersion() {
        String sdk = Build.VERSION.SDK;
        if (TextUtils.isEmpty(sdk)) {
            return "";
        } else {
            return sdk;
        }

    }

    /**
     * 获取手机的IMEI号
     */
    public static String getIMEI(Context sContext) {

        if (!TextUtils.isEmpty(strIMEI)) {
            return strIMEI;
        }
        strIMEI = mDeviceId;
        if (!checkPermission(sContext, Manifest.permission.READ_PHONE_STATE)) {
            return getVirutalIMEI();
        }
        TelephonyManager manager = (TelephonyManager) sContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            try {
                if (TextUtils.isEmpty(mDeviceId)) {
                    mDeviceId = manager.getDeviceId();
                    strIMEI = mDeviceId;
                }
            } catch (Throwable ignore) {
                CmlLogUtil.et(ignore);
            }
        }
        if (isSameChar(strIMEI)) {
            strIMEI = null;
        }

        if (strIMEI == null || strIMEI.length() == 0 || strIMEI.equals("null")) {
            strIMEI = "35"
                    + Build.BOARD.length() % 10
                    + Build.BRAND.length() % 10
                    + Build.CPU_ABI.length() % 10
                    + Build.DEVICE.length() % 10
                    + Build.DISPLAY.length() % 10
                    + Build.HOST.length() % 10
                    + Build.ID.length() % 10
                    + Build.MANUFACTURER.length() % 10
                    + Build.MODEL.length() % 10
                    + Build.PRODUCT.length() % 10
                    + Build.TAGS.length() % 10
                    + Build.TYPE.length() % 10
                    + Build.USER.length() % 10;
        }

        strIMEI += getLastModifiedMD5Str();
        return strIMEI;
    }

    private static String getVirutalIMEI() {
        return "35"
                + Build.BOARD.length() % 10
                + Build.BRAND.length() % 10
                + Build.CPU_ABI.length() % 10
                + Build.DEVICE.length() % 10
                + Build.DISPLAY.length() % 10
                + Build.HOST.length() % 10
                + Build.ID.length() % 10
                + Build.MANUFACTURER.length() % 10
                + Build.MODEL.length() % 10
                + Build.PRODUCT.length() % 10
                + Build.TAGS.length() % 10
                + Build.TYPE.length() % 10
                + Build.USER.length() % 10;
    }

    /**
     * 判断字符串是否为同一个字符组成
     */
    public static boolean isSameChar(String str) {
        if (str == null || "".equals(str)) {
            return false;
        }
        for (int i = 0; i < str.length() - 1; i++) {
            if (str.charAt(i) != str.charAt(i + 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取网络类型
     *
     * @return 当前的网络类型
     */
    public static String getNetworkType(Context context) {
        String name = "UNKNOWN";

        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        try {
            networkInfo = connMgr.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (networkInfo != null) {
            if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                return "WIFI";
            }
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return name;
        }

        int type = tm.getNetworkType();
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                name = "2G";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                name = "3G";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                name = "4G";
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                name = "UNKNOWN";
                break;
            default:
                name = "UNKNOWN";
                break;
        }
        return name;
    }

    /**
     * 获取系统文件的属性字符串
     */
    private static String getLastModifiedMD5Str() {
        String path = "/system/build.prop";
        File f = new File(path);
        Long modified = f.lastModified();

        char[] data = md5(modified.toString());

        if (data == null) {
            return null;
        } else {
            return new String(data);
        }
    }

    /**
     * 生成md5串
     */
    private static char[] md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            int count = (messageDigest.length << 1);
            char[] data = new char[count];
            int tmp;
            byte idx = 0;
            for (int i = 0; i < count; i += 2) {
                tmp = messageDigest[idx] & 0xFF;
                idx++;
                if (tmp < 16) {
                    data[i] = '0';
                    data[i + 1] = getHexChar(tmp);
                } else {
                    data[i] = getHexChar(tmp >> 4);
                    data[i + 1] = getHexChar(tmp & 0xF);
                }
            }
            return data;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * int转char
     */
    private static char getHexChar(int value) {
        if (value < 10) {
            return (char) ('0' + value);
        } else {
            return (char) ('A' + value - 10);
        }
    }

    /**
     * 检查某权限是否存在存在，异常则返回 false
     */
    public static boolean checkPermission(Context context, String permission) {
        boolean b = checkPermission(context, permission, false);
        if (!b) {
            CmlLogUtil.e(TAG, " permission:" + permission + "  disable");
        }
        return b;
    }

    /**
     * 检查某权限是否存在存在，异常则返回 defaultValue
     */
    private static boolean checkPermission(Context context, String permission, boolean defalutValue) {
        try {
            return PackageManager.PERMISSION_GRANTED == context.checkCallingOrSelfPermission(permission);
        } catch (Exception e) {
            return defalutValue;
        }
    }


}
