package com.didi.chameleon.sdk.extend;

import android.content.Context;
import android.text.TextUtils;

import com.didi.chameleon.sdk.CmlConstant;
import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlActivityInstance;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlCallbackSimple;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlModule;
import com.didi.chameleon.sdk.module.CmlModuleManager;
import com.didi.chameleon.sdk.module.CmlParam;
import com.didi.chameleon.sdk.utils.CmlSystemUtil;
import com.didi.chameleon.sdk.utils.CmlViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

@CmlModule(alias = "cml")
public class CmlCommonModule {

    @CmlMethod(alias = "setTitle")
    public void updateNaviTitle(ICmlActivityInstance instance, @CmlParam(name = "title") String title) {
        instance.updateNaviTitle(title);
    }

    @CmlMethod(alias = "openPage")
    public void openNativePage(ICmlInstance instance, Context context,
                               @CmlParam(name = "url") String url, @CmlParam(name = "closeCurrent") boolean closeWeb) {
        if (closeWeb) {
            instance.finishSelf();
        }
        CmlEnvironment.getNavigatorAdapter().navigator(context, url);
    }

    @CmlMethod(alias = "closePage")
    public void closePage(ICmlInstance instance) {
        instance.finishSelf();
    }

    @CmlMethod(alias = "reloadPage")
    public void reloadPage(ICmlInstance instance, @CmlParam(name = "url") String url) {
        instance.reload(url);
    }

    @CmlMethod(alias = "rollbackWeb")
    public void rollbackWeb(ICmlInstance instance) throws IllegalStateException {
        instance.degradeToH5(CmlConstant.FAILED_TYPE_DEGRADE);
    }

    @CmlMethod(alias = "getLaunchUrl", uiThread = false)
    public void getLaunchUrl(ICmlInstance instance, CmlCallback<String> callback) {
        String launchUrl = instance.getCurrentURL();
        if (TextUtils.isEmpty(launchUrl)) {
            callback.onError(CmlCallback.ERROR_DEFAULT);
        } else {
            callback.onCallback(instance.getTargetURL());
        }
    }

    @CmlMethod(alias = "getSystemInfo", uiThread = false)
    public void getSystemInfo(ICmlInstance instance, CmlCallback<JSONObject> callback) {
        Context context = instance.getContext().getApplicationContext();
        JSONObject object = new JSONObject();
        JSONObject extObject = new JSONObject();
        try {
            object.put("scale", CmlViewUtil.getDensity(context));
            object.put("deviceWidth", CmlViewUtil.getScreenWidth(context));
            object.put("deviceHeight", CmlViewUtil.getScreenHeight(context));
            object.put("os", "android");
            extObject.put("model", CmlSystemUtil.getModel());
            extObject.put("imei", CmlSystemUtil.getIMEI(context));
            extObject.put("netType", CmlSystemUtil.getNetworkType(context));
            extObject.put("statusbarHeight", CmlViewUtil.getStatusBarHeight(context));
            extObject.put("navigationHeight", CmlViewUtil.getVirtualBarHeight(context));
            extObject.put("viewHeight", instance.getObjectView() == null ? 0 : instance.getObjectView().getHeight());
            object.put("extraParams", extObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        callback.onCallback(object);
    }

    @CmlMethod(alias = "getSDKInfo", uiThread = false)
    public void getSDKInfo(CmlCallback<JSONObject> callback) {
        JSONObject object = new JSONObject();
        try {
            object.put("version", CmlEnvironment.VERSION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        callback.onCallback(object);
    }

    @CmlMethod(alias = "canIUse", uiThread = false)
    public void canUseAction(@CmlParam(name = "module") String module, @CmlParam(name = "method") String method,
                             CmlCallbackSimple callback) {
        if (TextUtils.isEmpty(module) || TextUtils.isEmpty(method)) {
            callback.onFail();
            return;
        }
        boolean isHas = CmlModuleManager.getInstance().containsAction(module, method);
        if (isHas) {
            callback.onSuccess();
        } else {
            callback.onFail();
        }
    }

    @CmlMethod(alias = "broadcast")
    public void broadcast(String data) {
        for (ICmlInstance instance : CmlInstanceManage.getInstance().getInstanceList()) {
            CmlModuleManager.getInstance().invokeWeb(instance.getInstanceId(),
                    "cml", "onBroadcast", data, null);
        }
    }

}
