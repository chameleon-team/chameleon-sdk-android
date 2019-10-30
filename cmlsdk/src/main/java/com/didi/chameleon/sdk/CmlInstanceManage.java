package com.didi.chameleon.sdk;

import android.support.annotation.NonNull;

import com.didi.chameleon.sdk.utils.CmlLogUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 页面渲染容器实例的管理类
 * 每一个容器在new 自己对应的 instance 时注册到此管理类里面，通过 instanceId 来进行存取。
 */
public class CmlInstanceManage {

    public interface CmlInstanceChangeListener {

        void onAddInstance(String instanceId);

        void onRemoveInstance(String instanceId);

    }

    public interface CmlInstanceDestroyListener {
        void onDestroy();
    }

    public static final String TAG = "CmlInstanceManage";

    private static CmlInstanceManage instance = new CmlInstanceManage();

    private List<CmlInstanceChangeListener> mListener = new LinkedList<>();
    private Map<String, List<CmlInstanceDestroyListener>> mDestroyListener = new HashMap<>();

    private HashMap<String, ICmlInstance> mInstances = new HashMap<>();
    private HashMap<String, ICmlLaunchCallback> mLaunchCallbacks = new HashMap<>();

    public static CmlInstanceManage getInstance() {
        return instance;
    }

    public void registerListener(CmlInstanceChangeListener listener) {
        mListener.add(listener);
    }

    public void registerDestroyListener(String instanceId, CmlInstanceDestroyListener listener) {
        if (!mInstances.containsKey(instanceId)) {
            listener.onDestroy();
            return;
        }
        if (mDestroyListener.containsKey(instanceId)) {
            mDestroyListener.get(instanceId).add(listener);
        } else {
            List<CmlInstanceDestroyListener> list = new LinkedList<>();
            list.add(listener);
            mDestroyListener.put(instanceId, list);
        }
    }

    @NonNull
    public ICmlInstance getCmlInstance(String instanceId) {
        if (mInstances.containsKey(instanceId)) {
            return mInstances.get(instanceId);
        }
        CmlLogUtil.w(TAG, "getCmlInstance empty! " + instanceId);
        return ICmlInstance.empty;
    }

    @NonNull
    public ICmlViewInstance getCmlViewInstance(String instanceId) {
        ICmlInstance instance = mInstances.get(instanceId);
        if (instance instanceof ICmlViewInstance) {
            return (ICmlViewInstance) instance;
        }
        CmlLogUtil.w(TAG, "getCmlViewInstance empty! " + instanceId);
        return ICmlViewInstance.empty;
    }

    @NonNull
    public ICmlActivityInstance getCmlActivityInstance(String instanceId) {
        ICmlInstance instance = mInstances.get(instanceId);
        if (instance instanceof ICmlActivityInstance) {
            return (ICmlActivityInstance) instance;
        }
        CmlLogUtil.w(TAG, "getCmlActivityInstance empty! " + instanceId);
        return ICmlActivityInstance.empty;
    }

    public ICmlLaunchCallback getLaunchCallback(String instanceId) {
        if (mLaunchCallbacks.containsKey(instanceId)) {
            return mLaunchCallbacks.get(instanceId);
        }
        return null;
    }

    public void addLaunchCallback(String instanceId, ICmlLaunchCallback launchCallback) {
        mLaunchCallbacks.put(instanceId, launchCallback);
    }

    public void removeLaunchCallback(String instanceId) {
        mLaunchCallbacks.remove(instanceId);
    }

    public void addInstance(ICmlInstance instance) {
        mInstances.put(instance.getInstanceId(), instance);
        for (CmlInstanceChangeListener listener : mListener) {
            listener.onAddInstance(instance.getInstanceId());
        }
    }

    public void removeInstance(String instanceId) {
        mInstances.remove(instanceId);
        for (CmlInstanceChangeListener listener : mListener) {
            listener.onRemoveInstance(instanceId);
        }
        List<CmlInstanceDestroyListener> list = mDestroyListener.remove(instanceId);
        if (list != null) {
            for (CmlInstanceDestroyListener listener : list) {
                listener.onDestroy();
            }
        }
    }

    public Collection<ICmlInstance> getInstanceList() {
        if (mInstances == null) {
            return Collections.emptyList();
        }
        return mInstances.values();
    }
}
