package com.didi.chameleon.sdk.module;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.didi.chameleon.sdk.utils.CmlLogUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CmlModuleStore {

    private CmlModuleMediator mediator;

    private Map<String, ModuleHolder> moduleMap = new HashMap<>();
    private Map<Class, Object> globalMap = new HashMap<>();
    private Map<String, Map<Class, Object>> instanceMap = new HashMap<>();
    private Map<String, CmlCallback> callbackMap = new HashMap<>();

    public CmlModuleStore(CmlModuleMediator mediator) {
        this.mediator = mediator;
    }

    public <T> void storeModule(@NonNull Class<T> moduleClass) {
        ModuleHolder moduleHolder = parseClassModule(moduleClass);
        ModuleHolder currentHolder = moduleMap.get(moduleHolder.getName());
        if (currentHolder == null) {
            moduleMap.put(moduleHolder.getName(), moduleHolder);
        } else if (!moduleHolder.isUseful()) {
            currentHolder.addJoin(moduleHolder.joinSet);
        } else if (!currentHolder.isUseful()) {
            moduleHolder.addJoin(currentHolder.joinSet);
            moduleMap.put(moduleHolder.getName(), moduleHolder);
        } else {
            CmlLogUtil.e("cml module", "already has module name " + moduleHolder.getName());
        }
    }

    public boolean containsAction(@NonNull String moduleName, @NonNull String methodName) {
        ModuleHolder moduleHolder = moduleMap.get(moduleName);
        if (moduleHolder == null || !moduleHolder.isUseful()) {
            return false;
        }
        Map<String, MethodHolder> methodHolderMap = moduleHolder.getMethodMap();
        return methodHolderMap.containsKey(methodName);
    }

    public void removeInstance(@NonNull String instanceId) {
        instanceMap.remove(instanceId);
    }

    @NonNull
    public CmlModuleMediator.Info getMediatorInfo(String instanceId, String moduleName, String methodName)
            throws CmlModuleException, CmlMethodException {
        CmlModuleMediator.Info info = new CmlModuleMediator.Info();

        ModuleHolder holder = moduleMap.get(moduleName);
        if (holder == null || !holder.isUseful()) {
            throw CmlModuleException.throwNotFound(moduleName);
        }

        Map<String, MethodHolder> methodMap = holder.getMethodMap();
        MethodHolder methodHolder = methodMap.get(methodName);
        if (methodHolder == null) {
            throw CmlMethodException.throwNotFound(moduleName, methodName);
        }

        if (holder.isGlobal()) {
            info.object = getGlobalObject(methodHolder.objClass);
        } else {
            info.object = getInstanceObject(instanceId, methodHolder.objClass);
        }

        info.method = methodHolder.method;
        info.isUiThread = methodHolder.isUiThread();
        info.paramKey = methodHolder.getParamsKey();
        info.paramAdmin = methodHolder.getParamsAdmin();

        return info;
    }

    private Object getGlobalObject(Class moduleClass)
            throws CmlModuleException {
        Object obj = globalMap.get(moduleClass);
        if (obj == null) {
            obj = mediator.newInstance(null, moduleClass);
            globalMap.put(moduleClass, obj);
        }
        return obj;
    }

    private Object getInstanceObject(@NonNull String instanceId, @NonNull Class moduleClass)
            throws CmlModuleException {
        Map<Class, Object> objectMap = instanceMap.get(instanceId);
        if (objectMap == null) {
            objectMap = new HashMap<>();
            instanceMap.put(instanceId, objectMap);
        }
        Object obj = objectMap.get(moduleClass);
        if (obj == null) {
            obj = mediator.newInstance(instanceId, moduleClass);
            objectMap.put(moduleClass, obj);
        }
        return obj;
    }

    @NonNull
    public <T> String stashCallback(String instanceId, CmlCallback<T> callback) {
        String callbackId = mediator.createCallbackId(instanceId);
        callbackMap.put(callbackId, new CmlCallbackProxy<>(instanceId, callbackId, callback));
        return callbackId;
    }

    @Nullable
    public CmlCallback dropCallback(String instanceId, String callbackId) {
        return callbackMap.remove(callbackId);
    }

    @NonNull
    private static <T> ModuleHolder parseClassModule(@NonNull Class<T> moduleClass) {
        CmlJoin join = moduleClass.getAnnotation(CmlJoin.class);
        if (join == null) {
            CmlModule annotation = moduleClass.getAnnotation(CmlModule.class);
            return new ModuleHolder(moduleClass, annotation);
        } else {
            String name = join.name();
            return new ModuleHolder(name, moduleClass);
        }
    }

    @NonNull
    private static List<MethodHolder> parseModuleMethod(@NonNull Class moduleClass, @Nullable Collection<Class> joinClass) {
        List<Class> classList = new LinkedList<>();
        classList.add(moduleClass);
        if (joinClass != null) {
            classList.addAll(joinClass);
        }
        List<MethodHolder> methodHolders = new LinkedList<>();
        for (Class c : classList) {
            for (Method method : c.getMethods()) {
                if (!Modifier.isStatic(method.getModifiers())
                        && method.getAnnotation(CmlIgnore.class) == null) {
                    CmlMethod methodAn = method.getAnnotation(CmlMethod.class);
                    methodHolders.add(new MethodHolder(c, method, methodAn));
                }
            }
        }
        return methodHolders;
    }

    @NonNull
    private static CmlParam[] parseMethodParam(@NonNull Method method) {
        Class[] paraClass = method.getParameterTypes();
        CmlParam[] paramInfo = new CmlParam[paraClass.length];
        Annotation[][] paramsAns = method.getParameterAnnotations();
        for (int i = 0, len = paraClass.length; i < len; i++) {
            Annotation[] paramAns = paramsAns[i];
            for (Annotation annotation : paramAns) {
                if (annotation instanceof CmlParam) {
                    paramInfo[i] = (CmlParam) annotation;
                }
            }
        }
        return paramInfo;
    }

    private static class ModuleHolder {

        private String name;

        Class moduleClass;

        @Nullable
        private CmlModule annotation;

        @Nullable
        private Map<String, MethodHolder> methodMap;

        private Set<Class> joinSet;

        ModuleHolder(Class moduleClass, @Nullable CmlModule annotation) {
            this.moduleClass = moduleClass;
            this.annotation = annotation;
        }

        ModuleHolder(String name, Class joinClass) {
            this.name = name;
            addJoin(Collections.singleton(joinClass));
        }

        boolean isUseful() {
            return moduleClass != null;
        }

        String getName() {
            if (name == null) {
                if (annotation == null) {
                    name = moduleClass.getSimpleName();
                } else {
                    String alias = annotation.alias();
                    if (TextUtils.isEmpty(alias)) {
                        name = moduleClass.getSimpleName();
                    } else {
                        name = alias;
                    }
                }
            }
            return name;
        }

        boolean isGlobal() {
            return annotation == null || annotation.global();
        }

        @NonNull
        Map<String, MethodHolder> getMethodMap() {
            if (methodMap == null) {
                methodMap = new HashMap<>();
                List<MethodHolder> methodHolders = parseModuleMethod(moduleClass, joinSet);
                for (MethodHolder methodHolder : methodHolders) {
                    methodMap.put(methodHolder.getName(), methodHolder);
                }
            }
            return methodMap;
        }

        void addJoin(Collection<Class> joinClass) {
            if (joinSet == null) {
                joinSet = new HashSet<>();
            }
            joinSet.addAll(joinClass);
            methodMap = null;
        }

    }

    private static class MethodHolder {

        Class objClass;

        Method method;

        @Nullable
        private CmlMethod annotation;

        @Nullable
        private CmlParam[] paramInfo;

        MethodHolder(Class objClass, Method method, @Nullable CmlMethod annotation) {
            this.objClass = objClass;
            this.method = method;
            this.annotation = annotation;
        }

        String getName() {
            if (annotation == null) {
                return method.getName();
            } else {
                String alias = annotation.alias();
                if (TextUtils.isEmpty(alias)) {
                    return method.getName();
                } else {
                    return alias;
                }
            }
        }

        boolean isUiThread() {
            return annotation == null || annotation.uiThread();
        }

        @NonNull
        String[] getParamsKey() {
            if (paramInfo == null) {
                paramInfo = parseMethodParam(method);
            }
            String[] paramKey = new String[paramInfo.length];
            for (int i = 0; i < paramInfo.length; i++) {
                String name = paramInfo[i] == null ? null : paramInfo[i].name();
                if (!TextUtils.isEmpty(name)) {
                    paramKey[i] = name;
                }
            }
            return paramKey;
        }

        @NonNull
        String[] getParamsAdmin() {
            if (paramInfo == null) {
                paramInfo = parseMethodParam(method);
            }
            String[] paramAdmin = new String[paramInfo.length];
            for (int i = 0; i < paramInfo.length; i++) {
                String admin = paramInfo[i] == null ? null : paramInfo[i].admin();
                if (!TextUtils.isEmpty(admin)) {
                    paramAdmin[i] = admin;
                }
            }
            return paramAdmin;
        }
    }

}
