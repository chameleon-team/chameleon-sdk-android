package com.didi.chameleon.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CmlCodeUtil {

    public static String encodeUrl(String in) throws UnsupportedEncodingException {
        String out = URLEncoder.encode(in, "UTF-8");
        // html4与RFC-3986针对空格的编码方式不同会变成+号，兼容后将+号替换为空格
        out = out.replaceAll("\\+", "%20");
        return out;
    }

}
