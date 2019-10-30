package com.didi.chameleon.weex.jsbundlemgr.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * limeihong
 * create at 2018/10/10
 */
public class CmlNetworkUtils {
    public static final int NET_NOT_AVAILABLE = 0;
    public static final int NET_WIFI = 2;
    public static final int NET_MOBILE = 1;
    public static final int NET_NOT_KNOW = 3;

    public static int getNetStatus(Context context) {
        if (null == context.getSystemService(Context.TELEPHONY_SERVICE)) {
            return NET_NOT_AVAILABLE;
        }
        switch (getNetworkType(context)) {
            case "UNKNOWN":
                return NET_NOT_KNOW;
            case "2G":
            case "3G":
            case "4G":
                return NET_MOBILE;
            case "WIFI":
                return NET_WIFI;
            default:
                return NET_NOT_KNOW;
        }
    }

    public static String getNetworkType(Context context) {
        String name = "UNKNOWN";
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService("connectivity");
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && 1 == networkInfo.getType()) {
            return "WIFI";
        } else {
            TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
            if (tm == null) {
                return name;
            } else {
                int type = tm.getNetworkType();
                switch (type) {
                    case 0:
                        name = "UNKNOWN";
                        break;
                    case 1:
                    case 2:
                    case 4:
                    case 7:
                    case 11:
                        name = "2G";
                        break;
                    case 3:
                    case 5:
                    case 6:
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 14:
                    case 15:
                        name = "3G";
                        break;
                    case 13:
                        name = "4G";
                        break;
                    default:
                        name = "UNKNOWN";
                }

                return name;
            }
        }
    }
}
