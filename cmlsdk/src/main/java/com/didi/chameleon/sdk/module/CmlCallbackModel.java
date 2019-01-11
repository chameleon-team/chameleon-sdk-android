package com.didi.chameleon.sdk.module;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CmlCallbackModel<T> {

    private static final String CALLBACK_NO = "errno";
    private static final String CALLBACK_MSG = "msg";
    private static final String CALLBACK_DATA = "data";

    public int errorNo;

    public String msg;

    public T data;

    public static String toJson(CmlCallbackModel<String> model) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CALLBACK_NO, model.errorNo);
        jsonObject.put(CALLBACK_MSG, model.msg);
        jsonObject.put(CALLBACK_DATA, model.data);
        return jsonObject.toString();
    }

    public static CmlCallbackModel<String> fromJson(String str) throws JSONException {
        JSONObject jsonObject = new JSONObject(str);
        CmlCallbackModel<String> model = new CmlCallbackModel<>();
        model.errorNo = jsonObject.optInt(CALLBACK_NO);
        model.msg = jsonObject.optString(CALLBACK_MSG);
        model.data = jsonObject.optString(CALLBACK_DATA);
        return model;
    }

}
