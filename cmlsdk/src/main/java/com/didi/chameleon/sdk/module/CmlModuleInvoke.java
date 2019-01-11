package com.didi.chameleon.sdk.module;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.common.CmlThreadCenter;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Map;

public class CmlModuleInvoke {

    private static final String TAG = "CmlModuleInvoke";

    private CmlModuleMediator mediator;

    public CmlModuleInvoke(CmlModuleMediator mediator) {
        this.mediator = mediator;
    }

    public void invokeNative(@NonNull final String instanceId, @NonNull String moduleName, @NonNull String methodName,
                             @Nullable final String params, @Nullable final String callbackId)
            throws CmlModuleException, CmlMethodException {

        final CmlModuleMediator.Info info = mediator.getInvokeInfo(instanceId, moduleName, methodName);

        if (info.isUiThread) {
            CmlEnvironment.getThreadCenter().post(new CmlThreadCenter.IORunnable<Object[]>() {
                @Override
                protected Object[] run() {
                    return parseParams(info.method, params, info.paramKey, info.paramAdmin, instanceId, callbackId);
                }

                @Override
                protected void postRun(Object[] args) {
                    invokeAction(info.method, info.object, args,
                            checkAutoCallback(instanceId, callbackId, args));
                }
            });
        } else {
            CmlEnvironment.getThreadCenter().post(new Runnable() {
                @Override
                public void run() {
                    Object[] args = parseParams(info.method, params, info.paramKey, info.paramAdmin, instanceId, callbackId);
                    invokeAction(info.method, info.object, args,
                            checkAutoCallback(instanceId, callbackId, args));
                }
            });
        }

    }

    public void callbackNative(@NonNull String instanceId, @NonNull String callbackId, @Nullable final String params) {
        final CmlCallback callback = mediator.removeCallback(instanceId, callbackId);
        if (callback == null) {
            CmlLogUtil.w(TAG, "callback can't find callbackId-" + callbackId + " instanceId-" + instanceId);
        } else {
            if (callback.uiThread()) {
                CmlEnvironment.getThreadCenter().post(new CmlThreadCenter.IORunnable<CmlCallbackModel>() {
                    @Override
                    protected CmlCallbackModel run() {
                        return parseCallback(callback.dataClass, params);
                    }

                    @Override
                    protected void postRun(CmlCallbackModel arg) {
                        invokeCallback(callback, arg);
                    }
                });
            } else {
                CmlEnvironment.getThreadCenter().post(new Runnable() {
                    @Override
                    public void run() {
                        CmlCallbackModel arg = parseCallback(callback.dataClass, params);
                        invokeCallback(callback, arg);
                    }
                });
            }
        }
    }

    private void invokeAction(Method method, Object object, Object[] args, CmlCallbackSimple autoCallback) {
        try {
            method.invoke(object, args);
            if (autoCallback != null) {
                autoCallback.onSuccess();
            }
        } catch (Exception e) {
            if (autoCallback != null) {
                autoCallback.onFail();
            }
            if (CmlEnvironment.DEBUG) {
                CmlMethodException.throwInvokeFail(object, method, e).printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void invokeCallback(CmlCallback callback, CmlCallbackModel model) {
        try {
            if (model.errorNo == 0) {
                callback.onCallback(model.data);
            } else {
                callback.onError(model.errorNo, model.msg, model.data);
            }
        } catch (Exception e) {
            if (CmlEnvironment.DEBUG) {
                CmlMethodException.throwCallbackFail(callback, e).printStackTrace();
            }
        }
    }

    private CmlCallbackModel parseCallback(Class dataType, String param) throws IllegalArgumentException {
        try {
            CmlCallbackModel<String> model = CmlCallbackModel.fromJson(param);
            CmlCallbackModel objectModel = new CmlCallbackModel();
            objectModel.errorNo = model.errorNo;
            objectModel.msg = model.msg;
            objectModel.data = parseParam(dataType, model.data);
            return objectModel;
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Object[] parseParams(Method method, String params, String[] paramKeys, String[] paramAdmins,
                                 String instanceId, String callbackId) {
        Class<?>[] paramClass = method.getParameterTypes();
        int paramNum = paramClass.length;
        Object[] args = new Object[paramNum];
        for (int i = 0; i < paramNum; i++) {
            Class paramType = paramClass[i];
            String key = paramKeys[i];
            String admin = paramAdmins[i];

            if (CmlCallback.class.isAssignableFrom(paramType)) {
                args[i] = mediator.newCallback(instanceId, callbackId, paramType);
            } else if (mediator.isContextInfo(paramType)) {
                args[i] = mediator.getContextInfo(instanceId, paramType);
            } else if (key != null) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(params);
                } catch (JSONException e) {
                    CmlLogUtil.wt(CmlMethodException.throwParseParam(method, params));
                }
                String keyValue = jsonObject == null ? null : jsonObject.optString(key);
                args[i] = parseParam(paramType, TextUtils.isEmpty(keyValue) ? admin : keyValue);
            } else {
                args[i] = parseParam(paramType, TextUtils.isEmpty(params) ? admin : params);
            }
        }
        return args;
    }

    @NonNull
    private Object parseParam(@NonNull Class paramType, @Nullable String params)
            throws IllegalArgumentException {
        if (paramType == JSONObject.class) {
            try {
                return new JSONObject(params);
            } catch (JSONException e) {
                throw new IllegalArgumentException(e);
            }
        } else if (paramType == String.class) {
            return params == null ? "" : params;
        } else if (paramType == int.class) {
            try {
                return TextUtils.isEmpty(params) ? 0 : Integer.parseInt(params);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e);
            }
        } else if (paramType == double.class) {
            try {
                return TextUtils.isEmpty(params) ? 0.0 : Double.parseDouble(params);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e);
            }
        } else if (paramType == boolean.class) {
            try {
                return !TextUtils.isEmpty(params) && Boolean.parseBoolean(params);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            try {
                return CmlEnvironment.getJsonWrapper().fromJson(params, paramType);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Nullable
    public String wrapperParam(@Nullable Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return (String) object;
        } else if (object instanceof JSONObject) {
            return object.toString();
        } else if (object instanceof Map) {
            JSONObject jsonObject = new JSONObject();
            for (Object key : ((Map) object).keySet()) {
                try {
                    jsonObject.put(String.valueOf(key), ((Map) object).get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return wrapperParam(jsonObject);
        } else if (object.getClass() == int.class
                || object.getClass() == double.class
                || object.getClass() == boolean.class) {
            return String.valueOf(object);
        } else {
            return CmlEnvironment.getJsonWrapper().toJson(object);
        }
    }

    public String wrapperCallback(CmlCallbackModel model) throws Exception {
        String param = wrapperParam(model.data);
        if (!TextUtils.isEmpty(param)) {
            param = URLEncoder.encode(param, "UTF-8");
        }
        CmlCallbackModel<String> stringModel = new CmlCallbackModel<>();
        stringModel.errorNo = model.errorNo;
        stringModel.msg = model.msg;
        stringModel.data = param;
        return CmlCallbackModel.toJson(stringModel);
    }

    private CmlCallbackSimple checkAutoCallback(String instanceId, String callbackId, Object[] args) {
        if (TextUtils.isEmpty(callbackId)) {
            return null;
        }
        boolean hasCallback = false;
        if (args != null) {
            for (Object object : args) {
                if (object instanceof CmlCallback) {
                    hasCallback = true;
                    break;
                }
            }
        }
        if (!hasCallback) {
            return new CmlCallbackSimple(instanceId, callbackId);
        }
        return null;
    }

}
