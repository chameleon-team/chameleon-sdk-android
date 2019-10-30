package com.didi.chameleon.weex.jsbundlemgr.utils;

import android.net.Uri;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmlCodeUtils {
    /**
     * 公共前缀与变量之间的分割符号
     */
    private static final String VARIABLE_OUT_SEPARATOR = "??";
    /**
     * 变量与变量之间的分割符号
     */
    private static final String VARIABLE_INNER_SEPARATOR = ",";

    /**
     * 代码之间的分割符号的正则表达式
     */
    private static final String CODE_SEPARATOR = "\\/\\*=+\\(([a-zA-Z]+.*)\\)=+\\*\\/";

    /**
     * 从代码之间的分割符号中提出jsbundle地址的正则表达式
     */
    private static final String CODE_JSBUNDLE_SEPARATOR = "[^(]*.js";


    /**
     * 将一个url拆分成多个
     */
    public static String[] parseUrl(String url) {
        /*if (TextUtils.isEmpty(url)) {
            return null;
        }*/
        int index = url.indexOf(VARIABLE_OUT_SEPARATOR);
        if (index == -1) {
            // 不需要分割
            return new String[]{url};
        }
        String host = url.substring(0, index);
        url = url.replace(host + VARIABLE_OUT_SEPARATOR, "");
        String[] result = url.split(VARIABLE_INNER_SEPARATOR);
        if (result == null || result.length == 0) {
            return null;
        }
        for (int i = 0; i < result.length; i++) {
            String path = result[i];
            path = host + path;
            result[i] = path;
        }
        return result;
    }

    /**
     * 将多个url合并成一个url
     */
    public static String mergeUrl(List<String> urls) {
        if (urls == null || urls.size() == 0) {
            return null;
        }
        if (urls.size() == 1) {
            // 兼容单链接情况
            return urls.get(0);
        }
        String host = null;
        String[] pathArray = new String[urls.size()];
        int index = 0;
        for (String url : urls) {
            if (url == null) {
                continue;
            }
            Uri uri = Uri.parse(url);
            if (host == null) {
                host = "https://" + uri.getHost();
            }
            String path = uri.getPath();
            pathArray[index++] = path;

        }
        StringBuilder result = new StringBuilder(host);
        result.append(VARIABLE_OUT_SEPARATOR);
        for (int i = 0; i < pathArray.length; i++) {
            if (i != 0) {
                result.append(VARIABLE_INNER_SEPARATOR);
            }
            result.append(pathArray[i]);
        }
        return result.toString();
    }

    /**
     * @param code 整个jsBundle代码
     * @return 将整个jsBundle代码进行拆解，放到一个map里面，map的key是url，value是代码实体
     */
    public static Map<String, String> parseCode(String code) {
        if (code == null) {
            return null;
        }
        Pattern pattern = Pattern.compile(CODE_SEPARATOR);
        Pattern patternName = Pattern.compile(CODE_JSBUNDLE_SEPARATOR);
        String[] codeArray = pattern.split(code);
        Matcher matcher = pattern.matcher(code);
        String[] keys = new String[codeArray.length - 1];
        int index = 0;
        while (matcher.find()) {
            String name = matcher.group();
            // 从分隔符中提取出jsbundle地址
            Matcher matcherName = patternName.matcher(name);
            if (matcherName.find()) {
                keys[index++] = matcherName.group();
            }
        }
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < codeArray.length; i++) {
            if (i != 0) {
                result.put(keys[i - 1], codeArray[i]);
            }
        }
        return result;
    }


    /**
     * 将多个代码块合并成一个代码文件
     */
    public static String mergeCode(Map<String, String> codes) {
        if (codes == null || codes.size() == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (String value : codes.values()) {
            result.append(value);
        }
        return result.toString();
    }

    /**
     * url中的代码块是否都获取完成了
     */
    public static boolean isCodeFull(String url, Map<String, String> codes) {
        if (TextUtils.isEmpty(url) || codes == null || codes.size() == 0) {
            return false;
        }
        String[] urls = parseUrl(url);
        if (urls == null || urls.length == 0) {
            return false;
        }
        for (String s : urls) {
            if (codes.get(s) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从inputStream中解析出代码
     */
    public static String getCodeFromStream(InputStream inputStream) {
        return getString(inputStream);
    }

    /**
     * 将InputStream转为字符串
     *
     * @param stream 输入流
     * @return
     */
    private static String getString(InputStream stream) {
        if (stream == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = stream.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            try {
                stream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bos.toString();
    }

}
