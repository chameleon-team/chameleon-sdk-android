package com.didi.chameleon.sdk.module;

import android.support.annotation.NonNull;

public class CmlModuleException extends Exception {

    public static final int MODULE_NO_FOUND = 1;
    public static final int MODULE_INIT_FAIL = 2;

    public static CmlModuleException throwNotFound(@NonNull String moduleName) {
        String error = "the module " + moduleName + " is not found";
        return new CmlModuleException(error, MODULE_NO_FOUND);
    }

    public static CmlModuleException throwInitFail(@NonNull Class moduleClass, Throwable throwable) {
        String error = "the module " + moduleClass.getName() + " init failed";
        return new CmlModuleException(error, MODULE_INIT_FAIL, throwable);
    }

    private int errorCode;

    private CmlModuleException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    private CmlModuleException(String message, int errorCode, Throwable throwable) {
        super(message, throwable);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
