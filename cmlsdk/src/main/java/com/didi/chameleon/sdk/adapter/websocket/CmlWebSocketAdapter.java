package com.didi.chameleon.sdk.adapter.websocket;

import android.support.annotation.Nullable;

public interface CmlWebSocketAdapter {

    void connect(String url, @Nullable String protocol, EventListener listener);

    void send(String data);

    void close(int code, String reason);

    void destroy();

    interface EventListener {
        void onOpen();

        void onMessage(String data);

        void onClose(int code, String reason, boolean wasClean);

        void onError(String msg);
    }

}
