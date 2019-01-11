package com.didi.chameleon.weex.module;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.adapter.navigator.ICmlNavigatorAdapter;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.appfram.navigator.WXNavigatorModule;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.Constants;

/**

 * @since 18/6/15
 * 主要功能:
 */

public class CmlNavigatorModule extends WXNavigatorModule {
    @JSMethod(uiThread = true)
    public void open(JSONObject options, JSCallback success, JSCallback failure) {
        if (options != null) {
            String url = options.getString(Constants.Value.URL);
            JSCallback callback = success;
            JSONObject result = new JSONObject();
            if (!TextUtils.isEmpty(url)) {
                Uri rawUri = Uri.parse(url);
                String scheme = rawUri.getScheme();
                if (TextUtils.isEmpty(scheme) || Constants.Scheme.HTTP.equalsIgnoreCase(scheme) || Constants.Scheme.HTTPS.equalsIgnoreCase(scheme)) {
                    this.push(options.toJSONString(), success);
                } else {
                    try {
                        //路由跳转
                        ICmlNavigatorAdapter navigatorAdapter = CmlEnvironment.getNavigatorAdapter();
                        if (navigatorAdapter != null) {
                            boolean navigator = navigatorAdapter.navigator(mWXSDKInstance.getContext(), rawUri.toString());
                            if (!navigator) {
                                startActionView(rawUri);
                            }
                        } else {
                            startActionView(rawUri);
                        }
                        result.put(CALLBACK_RESULT, MSG_SUCCESS);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        result.put(CALLBACK_RESULT, MSG_FAILED);
                        result.put(CALLBACK_MESSAGE, "Open page failed.");
                        callback = failure;
                    }
                }
            } else {
                result.put(CALLBACK_RESULT, MSG_PARAM_ERR);
                result.put(CALLBACK_MESSAGE, "The URL parameter is empty.");
                callback = failure;
            }

            if (callback != null) {
                callback.invoke(result);
            }
        }
    }

    private void startActionView(Uri rawUri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, rawUri);
            ComponentName componentName = intent.resolveActivity(mWXSDKInstance.getContext().getPackageManager());
            if (componentName != null) {
                mWXSDKInstance.getContext().startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
