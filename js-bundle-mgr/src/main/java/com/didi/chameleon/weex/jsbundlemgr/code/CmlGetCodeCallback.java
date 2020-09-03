package com.didi.chameleon.weex.jsbundlemgr.code;

import java.util.Map;

/**
 * @since 2018/9/10
 */

public interface CmlGetCodeCallback {

    void onSuccess(Map<String, String> codes, boolean fromLocal);

    void onFailed(String errMsg);

}
