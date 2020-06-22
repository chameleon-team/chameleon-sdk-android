package cn.zkml.care.member.module.record;

import android.Manifest;
import android.app.Activity;
import android.media.MediaPlayer;


import com.didi.chameleon.sdk.ICmlInstance;
import cn.zkml.care.member.module.record.permission.PermissionResultListener;
import cn.zkml.care.member.module.record.permission.PermissionChecker;
import cn.zkml.care.member.module.record.recorder.Constant;
import cn.zkml.care.member.module.record.recorder.ModuleResultListener;
import cn.zkml.care.member.module.record.recorder.RecorderModule;
import cn.zkml.care.member.module.record.recorder.Util;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlModule;
import com.didi.chameleon.sdk.module.CmlParam;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;


import org.apache.weex.common.WXModule;
import org.json.JSONException;
import org.json.JSONObject;

@CmlModule(alias = "record")
public class CmlRecordModule extends WXModule {


    HashMap<String, String> mCallParams;
    //JSCallback mCallCallback;
    CmlCallback<JSONObject> mCallCallback;

    String lang = Locale.getDefault().getLanguage();
    Boolean isChinese = lang.startsWith("zh");

    @CmlMethod(alias = "create")
    public void create(ICmlInstance instance,
                        final CmlCallback<JSONObject> callback)
        HashMap<String, String> params = new HashMap<>();
//        params.put("format", msg);
//        params.put("duration", String.valueOf(duration));
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

            mCallParams = params;
            mCallCallback = callback;

            PermissionChecker.requestPermissions((Activity) instance.getContext(), dialog, new PermissionResultListener() {
                @Override
                public void onResult(Boolean o) {
                    if ((boolean)o == true) callback.onCallback(Util.getError(Constant.RECORD_AUDIO_PERMISSION_DENIED, Constant.RECORD_AUDIO_PERMISSION_DENIED_CODE));
                }
            }, Constant.RECORD_AUDIO_PERMISSION_REQUEST_CODE,  Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            callback.onCallback(null);
        }
    }

    @CmlMethod(alias = "start")
    public void start(ICmlInstance instance,
                      @CmlParam(name = "format") String msg,
                      @CmlParam(name = "duration") int duration,
                      final CmlCallback<JSONObject> callback){
        HashMap<String, String> params = new HashMap<>();
        params.put("format", msg);
        params.put("duration", String.valueOf(duration));
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

            mCallParams = params;
            mCallCallback = callback;

            PermissionChecker.requestPermissions((Activity) instance.getContext(), dialog, new PermissionResultListener() {
                @Override
                public void onResult(Boolean o) {
                    if ((boolean)o == true) callback.onCallback(Util.getError(Constant.RECORD_AUDIO_PERMISSION_DENIED, Constant.RECORD_AUDIO_PERMISSION_DENIED_CODE));
                }
            }, Constant.RECORD_AUDIO_PERMISSION_REQUEST_CODE,  Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            realRecord(params, callback);
        }
    }

    @CmlMethod(alias = "pause", uiThread = false)
    public void pause(final CmlCallback<JSONObject> callback){
        RecorderModule.getInstance().pause(new ModuleResultListener() {
            @Override
            public void onResult(JSONObject o) {
                //jsCallback.invoke(o);
                callback.onCallback(o);
            }
        });
    }

    @CmlMethod(alias = "stop", uiThread = false)
    public void stop(final CmlCallback<JSONObject> callback){
        RecorderModule.getInstance().stop(new ModuleResultListener() {
            @Override
            public void onResult(JSONObject o) {
                callback.onCallback(o);
            }
        });
    }

    @Override
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
