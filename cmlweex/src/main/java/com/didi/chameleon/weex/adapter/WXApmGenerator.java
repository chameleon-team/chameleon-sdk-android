package com.didi.chameleon.weex.adapter;

import android.text.TextUtils;

import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.adapter.monitor.ICmlMonitorAdapter;

import org.apache.weex.WXSDKInstance;
import org.apache.weex.WXSDKManager;
import org.apache.weex.common.WXPerformance;
import org.apache.weex.performance.IApmGenerator;
import org.apache.weex.performance.IWXApmMonitorAdapter;
import org.apache.weex.performance.WXInstanceApm;

import java.util.HashMap;

public class WXApmGenerator implements IApmGenerator {

    private ICmlMonitorAdapter adapter;

    public WXApmGenerator(ICmlMonitorAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public IWXApmMonitorAdapter generateApmInstance(String type) {
        return new ApmMonitorAdapter();
    }

    private class ApmMonitorAdapter implements IWXApmMonitorAdapter {

        private ICmlInstance mCmlInstance;
        private WXSDKInstance mInstance;
        private WXPerformance mPerformance;
        private HashMap<String, Object> mStatMap = new HashMap<>();

        @Override
        public void onStart(String instanceId) {
            mInstance = WXSDKManager.getInstance().getAllInstanceMap().get(instanceId);
            if (mInstance != null) {
                mPerformance = mInstance.getWXPerformance();
                if (mCmlInstance == null) {
                    mCmlInstance = CmlInstanceManage.getInstance().getCmlInstance(mInstance.getInstanceId());
                }
            }
        }

        @Override
        public void onEnd() {

        }

        @Override
        public void onEvent(String name, Object value) {

        }

        @Override
        public void onStage(String name, long timestamp) {
            if (WXInstanceApm.KEY_PAGE_STAGES_NEW_FSRENDER.equals(name) && mPerformance != null) {
                mPerformance.newFsRenderTime = timestamp - mPerformance.renderUnixTimeOrigin;
            }
            if (WXInstanceApm.KEY_PAGE_STAGES_DESTROY.equals(name)) {
                try {
                    if (mCmlInstance != null && mPerformance != null) {
                        HashMap<String, Object> attrMap = new HashMap<>();
                        attrMap.putAll(mPerformance.getMeasureMap());
                        attrMap.putAll(mStatMap);
                        adapter.onTrace(mCmlInstance, ICmlMonitorAdapter.TRACE_PERFORMANCE, attrMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void addProperty(String key, Object value) {

        }

        @Override
        public void addStats(String key, double value) {
            if (!TextUtils.isEmpty(key)) {
                mStatMap.put(key, value);
            }
        }

        @Override
        public void onSubProcedureStage(String procedureName, String stageName) {

        }

        @Override
        public void onSubProcedureEvent(String procedureName, String eventName) {

        }

        @Override
        public void setSubProcedureStats(String procedureName, String name, double value) {

        }

        @Override
        public void setSubProcedureProperties(String procedureName, String name, Object value) {

        }

        @Override
        public void onAppear() {

        }

        @Override
        public void onDisappear() {

        }

        @Override
        public String parseReportUrl(String originUrl) {
            return null;
        }
    }
}
