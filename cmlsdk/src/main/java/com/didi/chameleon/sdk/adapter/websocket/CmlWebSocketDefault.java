package com.didi.chameleon.sdk.adapter.websocket;

import android.support.annotation.Nullable;

import com.didi.chameleon.sdk.adapter.CmlAdapterException;

import java.io.EOFException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * Chameleon 默认 WebSocket，基于 OkHttp3
 * Created by youzicong on 2018/10/12
 */
public class CmlWebSocketDefault implements CmlWebSocketAdapter {
    private static final String HEADER_SEC_WEB_SOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
    private WebSocket ws;
    private CmlWebSocketAdapter.EventListener eventListener;

    @Override
    public void connect(String url, @Nullable String protocol, final EventListener listener) {
        try {
            Class.forName("okhttp3.OkHttpClient");
        } catch (Exception e) {
            throw CmlAdapterException.throwAdapterNone(CmlWebSocketAdapter.class);
        }
        eventListener = listener;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        if (protocol != null) {
            builder.addHeader(HEADER_SEC_WEB_SOCKET_PROTOCOL, protocol);
        }
        builder.url(url);
        Request wsRequest = builder.build();
        ws = okHttpClient.newWebSocket(wsRequest, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                listener.onOpen();
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                listener.onMessage(text);
            }


            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                listener.onClose(code, reason, true);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                if (t instanceof EOFException) {
                    listener.onClose(CmlWebSocketCloseCodes.CLOSE_NORMAL.getCode(), CmlWebSocketCloseCodes.CLOSE_NORMAL.name(), true);
                } else {
                    listener.onError(t.getMessage());
                }
            }
        });
    }

    @Override
    public void send(String data) {
        if (ws != null) {
            try {
                ws.send(data);
            } catch (Exception e) {
                e.printStackTrace();
                reportError(e.getMessage());
            }
        } else {
            reportError("WebSocket is not ready");
        }
    }

    @Override
    public void close(int code, String reason) {
        if (ws != null) {
            try {
                ws.close(code, reason);
            } catch (Exception e) {
                reportError(e.getMessage());
            }
        }
    }

    @Override
    public void destroy() {
        if (ws != null) {
            try {
                ws.close(CmlWebSocketCloseCodes.CLOSE_GOING_AWAY.getCode(), CmlWebSocketCloseCodes.CLOSE_GOING_AWAY.name());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void reportError(String message) {
        if (eventListener != null) {
            eventListener.onError(message);
        }
    }
}
