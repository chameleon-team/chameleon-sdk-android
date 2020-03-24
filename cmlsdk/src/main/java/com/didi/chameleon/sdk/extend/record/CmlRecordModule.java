package com.didi.chameleon.sdk.extend.record;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

//import org.apache.weex.common.WXModule;

import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.extend.record.permission.ModuleResultListener;
import com.didi.chameleon.sdk.extend.record.permission.PermissionChecker;
import com.didi.chameleon.sdk.extend.record.recorder.Constant;
import com.didi.chameleon.sdk.extend.record.recorder.RecorderModule;
import com.didi.chameleon.sdk.extend.record.recorder.Util;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlModule;
import com.didi.chameleon.sdk.module.CmlParam;

import java.util.HashMap;
import java.util.Locale;



import org.json.JSONException;
import org.json.JSONObject;

@CmlModule(alias = "audio")
public class CmlRecordModule {


    HashMap<String, String> mCallParams;
    //JSCallback mCallCallback;
    CmlCallback<JSONObject> mCallCallback

    String lang = Locale.getDefault().getLanguage();
    Boolean isChinese = lang.startsWith("zh");

    @CmlMethod(alias = "start", uiThread = false)
    public void start(ICmlInstance instance,
                      @CmlParam(name = "format") String msg,
                      @CmlParam(name = "duration") int duration,
                      final CmlCallback<JSONObject> callback){
        boolean permAllow = PermissionChecker.lacksPermissions(instance.getContext(), Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permAllow) {
            HashMap<String, String> dialog = new HashMap<>();
            if (isChinese) {
                dialog.put("title", "权限申请");
                dialog.put("message", "请允许应用录制音频");
            } else {
                dialog.put("title", "Permission Request");
                dialog.put("message", "Please allow the app to record audio");
            }

            //mCallParams = params;
            mCallParams.put("format", msg)
            mCallParams.put("duration", String.valueOf(duration))
            mCallCallback = callback;

            PermissionChecker.requestPermissions((Activity) instance.getContext(), dialog, new ModuleResultListener() {
                @Override
                public void onResult(Boolean o) {
                    if ((boolean)o == true) callback.onCallback(Util.getError(Constant.RECORD_AUDIO_PERMISSION_DENIED, Constant.RECORD_AUDIO_PERMISSION_DENIED_CODE));
                }
            }, Constant.RECORD_AUDIO_PERMISSION_REQUEST_CODE,  Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            realRecord(params, jsCallback);
        }
    }

    @CmlMethod(alias = "pause", uiThread = false)
    public void pause(ICmlInstance instance, CmlCallback<JSONObject> callback){
        RecorderModule.getInstance().pause(new ModuleResultListener() {
            @Override
            public void onResult(JSONObject o) {
                //jsCallback.invoke(o);
                callback.onCallback(o);
            }
        });
    }

    @CmlMethod(alias = "stop", uiThread = false)
    public void stop(ICmlInstance instance, CmlCallback<JSONObject> callback){
        RecorderModule.getInstance().stop(new ModuleResultListener() {
            @Override
            public void onResult(JSONObject o) {
                callback.onCallback(o);
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constant.RECORD_AUDIO_PERMISSION_REQUEST_CODE) {
            if (PermissionChecker.hasAllPermissionsGranted(grantResults)) {
                realRecord(mCallParams, mCallCallback);
            } else {
                mCallCallback.onCallback(Util.getError(Constant.RECORD_AUDIO_PERMISSION_DENIED, Constant.RECORD_AUDIO_PERMISSION_DENIED_CODE));
            }
        }
    }

    public void realRecord(HashMap<String, String> params, final CmlCallback<JSONObject> callback){
        RecorderModule.getInstance().start(params, new ModuleResultListener() {
            @Override
            public void onResult(JSONObject o) {
                callback.onCallback(o);
            }
        });
    }

}
