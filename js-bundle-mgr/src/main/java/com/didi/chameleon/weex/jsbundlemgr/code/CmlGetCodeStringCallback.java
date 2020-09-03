package com.didi.chameleon.weex.jsbundlemgr.code;

/**
 * @since 2018/9/10
 */

public interface CmlGetCodeStringCallback {

    void onSuccess(String codes, boolean fromLocal);

    void onFailed(String errMsg);

}
