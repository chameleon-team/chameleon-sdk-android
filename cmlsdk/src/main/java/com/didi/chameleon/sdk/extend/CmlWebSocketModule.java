package com.didi.chameleon.sdk.extend;

import android.os.Looper;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.adapter.websocket.CmlWebSocketAdapter;
import com.didi.chameleon.sdk.adapter.websocket.CmlWebSocketCloseCodes;
import com.didi.chameleon.sdk.module.CmlMethod;
import com.didi.chameleon.sdk.module.CmlModule;
import com.didi.chameleon.sdk.module.CmlModuleManager;
import com.didi.chameleon.sdk.module.CmlParam;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

import java.util.HashMap;
import java.util.Map;

@CmlModule(alias = "webSocket", global = false)
public class CmlWebSocketModule {

    private static final String TAG = "WebSocketModule";
    private static final String KEY_DATA = "data";
    private static final String KEY_CODE = "code";
    private static final String KEY_REASON = "reason";
    private static final String KEY_WAS_CLEAN = "wasClean";

    private String instanceId;
    private CmlWebSocketAdapter webSocketAdapter;
    private WebSocketAdapter eventListener;

    public CmlWebSocketModule(ICmlInstance instance) {
        CmlLogUtil.e(TAG, "create new instance");
        instanceId = instance.getInstanceId();
        CmlInstanceManage.getInstance().registerDestroyListener(instance.getInstanceId(), new CmlInstanceManage.CmlInstanceDestroyListener() {
            @Override
            public void onDestroy() {
                destroy();
            }
        });
    }

    @CmlMethod(alias = "WebSocket", uiThread = false)
    public void WebSocket(@CmlParam(name = "url") String url, @CmlParam(name = "protocol") String protocol) {
        if (webSocketAdapter != null) {
            CmlLogUtil.w(TAG, "close");
            webSocketAdapter.close(CmlWebSocketCloseCodes.CLOSE_GOING_AWAY.getCode(), CmlWebSocketCloseCodes.CLOSE_GOING_AWAY.name());
        }
        webSocketAdapter = CmlEnvironment.getWebSocketAdapter();
        eventListener = new WebSocketAdapter();
        webSocketAdapter.connect(url, protocol, eventListener);
    }

    @CmlMethod(alias = "send", uiThread = false)
    public void send(String data) {
        webSocketAdapter.send(data);
    }

    @CmlMethod(alias = "close", uiThread = false)
    public void close(@CmlParam(name = "code") String code, @CmlParam(name = "reason") String reason) {
        int codeNumber = CmlWebSocketCloseCodes.CLOSE_NORMAL.getCode();
        if (code != null) {
            try {
                codeNumber = Integer.parseInt(code);
            } catch (NumberFormatException ignore) {
            }
        }
        webSocketAdapter.close(codeNumber, reason);
    }

    private void destroy() {
        Runnable destroyTask = new Runnable() {
            @Override
            public void run() {
                CmlLogUtil.w(TAG, "close session with instance " + instanceId);
                if (webSocketAdapter != null) {
                    webSocketAdapter.destroy();
                }
                webSocketAdapter = null;
                eventListener = null;
                instanceId = null;
            }
        };

        if (Looper.myLooper() == Looper.getMainLooper()) {
            CmlEnvironment.getThreadCenter().post(destroyTask);
        } else {
            destroyTask.run();
        }
    }

    private class WebSocketAdapter implements CmlWebSocketAdapter.EventListener {

        @Override
        public void onOpen() {
            CmlModuleManager.getInstance().invokeWeb(instanceId,
                    "webSocket", "onopen", null, null);
        }

        @Override
        public void onMessage(String data) {
            Map<String, String> msg = new HashMap<>(1);
            msg.put(KEY_DATA, data);
            CmlModuleManager.getInstance().invokeWeb(instanceId,
                    "webSocket", "onmessage", msg, null);
        }

        @Override
        public void onClose(int code, String reason, boolean wasClean) {
            Map<String, Object> msg = new HashMap<>(3);
            msg.put(KEY_CODE, code);
            msg.put(KEY_REASON, reason);
            msg.put(KEY_WAS_CLEAN, wasClean);
            CmlModuleManager.getInstance().invokeWeb(instanceId,
                    "webSocket", "onclose", msg, null);
        }

        @Override
        public void onError(String msg) {
            Map<String, String> info = new HashMap<>(1);
            info.put(KEY_DATA, msg);
            CmlModuleManager.getInstance().invokeWeb(instanceId,
                    "webSocket", "onerror", info, null);
        }
    }

}
