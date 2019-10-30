package com.didi.chameleon.sdk.bridge;

import android.text.TextUtils;

public class CmlProtocol {
    private String action;
    private String module;
    private String method;
    private String args;
    private String callbackId;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public void setCallbackId(String callbackId) {
        this.callbackId = callbackId;
    }

    public boolean isActionEmpty() {
        return TextUtils.isEmpty(action);
    }

    public boolean isModuleEmpty() {
        return TextUtils.isEmpty(module);
    }

    public boolean isMethodEmpty() {
        return TextUtils.isEmpty(method);
    }

    public boolean isCallbackIdEmpty() {
        return TextUtils.isEmpty(callbackId);
    }
}
