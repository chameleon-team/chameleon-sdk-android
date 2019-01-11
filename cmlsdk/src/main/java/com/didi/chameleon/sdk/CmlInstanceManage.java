package com.didi.chameleon.sdk;

import android.content.Context;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 页面渲染容器实例的管理类
 * 每一个容器在new 自己对应的 instance 时注册到此管理类里面，通过 instanceId 来进行存取。
 */
public class CmlInstanceManage {

    public interface CmlInstanceChangeListener {

        void onAddInstance(String instanceId);

        void onRemoveInstance(String instanceId);

    }

    private static CmlInstanceManage instance = new CmlInstanceManage();

    private List<CmlInstanceChangeListener> mListener = new LinkedList<>();
    private HashMap<String, ICmlInstance> mInstances = new HashMap<>();
    private HashMap<String, ICmlActivityInstance> mActivityInstances = new HashMap<>();
    private HashMap<String, ICmlViewInstance> mViewInstances = new HashMap<>();

    public static CmlInstanceManage getInstance() {
        return instance;
    }

    public void registerListener(CmlInstanceChangeListener listener) {
        mListener.add(listener);
    }

    public ICmlInstance getCmlInstance(String instanceId) {
        if (mInstances.containsKey(instanceId)) {
            return mInstances.get(instanceId);
        }
        return null;
    }

    public ICmlViewInstance getCmlViewInstance(String instanceId) {
        if (mViewInstances.containsKey(instanceId)) {
            return mViewInstances.get(instanceId);
        }
        return null;
    }

    public ICmlActivityInstance getCmlActivityInstance(String instanceId) {
        if (mActivityInstances.containsKey(instanceId)) {
            return mActivityInstances.get(instanceId);
        }
        return null;
    }

    public void addActivityInstance(Context context, String instanceId, ICmlActivityInstance instance) {
        mInstances.put(instanceId, instance);
        mActivityInstances.put(instanceId, instance);
        for (CmlInstanceChangeListener listener : mListener) {
            listener.onAddInstance(instanceId);
        }
    }

    public void removeActivityInstance(String instanceId) {
        mInstances.remove(instanceId);
        mActivityInstances.remove(instanceId);
        for (CmlInstanceChangeListener listener : mListener) {
            listener.onRemoveInstance(instanceId);
        }
    }

    public void addViewInstance(Context context, String instanceId, ICmlViewInstance instance) {
        mInstances.put(instanceId, instance);
        mViewInstances.put(instanceId, instance);
        for (CmlInstanceChangeListener listener : mListener) {
            listener.onAddInstance(instanceId);
        }
        // do something here
    }

    public void removeViewInstance(String instanceId) {
        // do something here
        mInstances.remove(instanceId);
        mViewInstances.remove(instanceId);
        for (CmlInstanceChangeListener listener : mListener) {
            listener.onRemoveInstance(instanceId);
        }
    }
}
