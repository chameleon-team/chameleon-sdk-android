package com.didi.chameleon.web.bridge;

import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.didi.chameleon.sdk.bridge.ICmlBridgeProtocol;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class CmlWebViewClient extends WebViewClient {
    private ICmlWebChannel mCmlWebChannel;

    public CmlWebViewClient(ICmlWebChannel cmlWebChannel) {
        mCmlWebChannel = cmlWebChannel;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith(ICmlBridgeProtocol.CML_SCHEME)) { // 如果是返回数据
            shouldOverrideUrlLoading(url);
            return true;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String url = request.getUrl().toString();
            if (url.startsWith(ICmlBridgeProtocol.CML_SCHEME)) { // 如果是返回数据
                shouldOverrideUrlLoading(url);
                return true;
            } else {
                return super.shouldOverrideUrlLoading(view, request);
            }
        } else {
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

    private void shouldOverrideUrlLoading(String protocol) {
        if (null != mCmlWebChannel) {
            mCmlWebChannel.channel(protocol);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

//        CmlWebBridgeUtil.webViewLoadLocalJs(view, ICmlBridgeProtocol.JS_CHANNEL_DEFINE);
    }
}
