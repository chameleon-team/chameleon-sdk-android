package cn.zkml.care.member.module.record.recorder;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

public class Util {
    public static JSONObject getError(String msg, int code){
        JSONObject result = new JSONObject();
        HashMap<String, Object> param = new HashMap<>();
        param.put("code", code);
        param.put("message", msg);
        try {
            result.put("error", param);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static float getScreenDpiX(Context context){
        return context.getResources().getDisplayMetrics().xdpi;
    }

    public static float getScreenDpiY(Context context){
        return context.getResources().getDisplayMetrics().ydpi;
    }

    public static float getDensity(Context context){
        return context.getResources().getDisplayMetrics().density;
    }

    public static float dp2px(Context context, float dp){
        return context.getResources().getDisplayMetrics().density * dp;
    }

    public static File getRootFile(){
        String package_name = CmlEngine.getInstance().getAppContext().getPackageName();
        String root_path = Environment.getExternalStorageDirectory() + "/" + package_name+ "/recorder";
        File file = new File(Constant.ROOT_PATH);
        if (!file.exists()) {
            Boolean res = file.mkdirs();
            CmlLogUtil.e("cmlBridge", res.toString());
        }
        return file;
    }

    public static File getFile(String fileName) throws IOException {
        File file = new File(getRootFile(), fileName);
        if (file.exists()) {
            file.delete();
            file.createNewFile();
        } else {
            file.createNewFile();
        }
        return file;
    }

    public static String getRootFilePath(){
        File file = new File(Constant.ROOT_PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }

    public static String getFilePath(String fileName) throws IOException {
        File file = new File(getRootFile(), fileName);
        if (file.exists()) {
            file.delete();
            file.createNewFile();
        } else {
            file.createNewFile();
        }
        return file.getAbsolutePath();
    }

    public static boolean isTel(String str){
        if (TextUtils.isEmpty(str)) return false;
        return isMobile(str) || isPhone(str);
    }

    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    public static boolean isPhone(String str) {
        Pattern p1 = null,p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if(str.length() >9)
        {   m = p1.matcher(str);
            b = m.matches();
        }else{
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    public static boolean isEmail(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    private static int MY_PERMISSIONS_EXTERNAL = 111;

    public static String getMD5(String val) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(val.getBytes());
        byte[] m = md5.digest();//加密
        return getString(m);
    }

    private static String getString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < b.length; i ++){
            sb.append(b[i]);
        }
        return sb.toString();
    }

}
