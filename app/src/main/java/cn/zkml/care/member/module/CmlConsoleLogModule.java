package cn.zkml.care.member.module;


import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlModule;
import com.didi.chameleon.sdk.module.CmlParam;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

import org.json.JSONException;
import org.json.JSONObject;


@CmlModule(alias = "consoleLog")
public class CmlConsoleLogModule {

    @CmlMethod(alias = "consoleLog", uiThread = false)
    public void consoleLog(@CmlParam(name = "tag") String tag,
                       @CmlParam(name = "msg") String msg,
                       CmlCallback<JSONObject> callback) {
        CmlLogUtil.e(tag, msg);
        JSONObject result = new JSONObject();
        callback.onCallback(result);
    }

}
