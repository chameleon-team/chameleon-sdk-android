package com.didi.chameleon.sdk.module;

import java.lang.reflect.Method;

public class CmlMethodException extends Exception {

    public static final int METHOD_NO_FOUND = 1;
    public static final int METHOD_PARSE_PARAM = 2;
    public static final int METHOD_INVOKE_FAIL = 3;
    public static final int METHOD_CALLBACK_FAIL = 4;

    public static CmlMethodException throwNotFound(String moduleName, String methodName) {
        String error = "the method " + methodName + " is not found in module " + moduleName;
        return new CmlMethodException(error, METHOD_NO_FOUND);
    }

    public static CmlMethodException throwParseParam(Method method, String param) {
        String error = "parse param \"" + param + "\" to json fail in method " + method.getName() + " at class " + method.getDeclaringClass().getSimpleName();
        return new CmlMethodException(error, METHOD_NO_FOUND);
    }

    public static CmlMethodException throwInvokeFail(Object module, Method method, Throwable throwable) {
        String error = "the method " + module.getClass().getName() + " is invoke failed by in module " + method.getName();
        return new CmlMethodException(error, METHOD_INVOKE_FAIL, throwable);
    }

    public static CmlMethodException throwCallbackFail(CmlCallback callback, Throwable throwable) {
        String callbackId = callback instanceof CmlCallbackProxy ? ((CmlCallbackProxy) callback).callbackId : "";
        String error = "the method callback invoke failed by  id " + callbackId;
        return new CmlMethodException(error, METHOD_CALLBACK_FAIL, throwable);
    }

    private int errorCode;

    private CmlMethodException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    private CmlMethodException(String message, int errorCode, Throwable throwable) {
        super(message, throwable);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
