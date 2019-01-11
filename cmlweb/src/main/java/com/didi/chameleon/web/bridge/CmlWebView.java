package com.didi.chameleon.web.bridge;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.utils.Util;
import com.didi.chameleon.web.CmlWebInstance;

public class CmlWebView extends WebView {
    public CmlWebView(Context context) {
        super(context);
        init(context);
    }

    public CmlWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CmlWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        WebSettings webSettings = getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(false);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false);

        if (Build.VERSION.SDK_INT >= 19 && Util.isApkDebug(context)) {
            setWebContentsDebuggingEnabled(true);
        }
        String agent = webSettings.getUserAgentString();
        if (agent.contains("cml")) {
            int lastIndex = agent.lastIndexOf("cml");
            agent = agent.substring(0, lastIndex - 1);
        }
        webSettings.setUserAgentString(agent + " cml/" + CmlEnvironment.VERSION);
    }

    public void startApplication(CmlWebInstance webInstance) {
        CmlWebBridgeModule webBridgeModule = new CmlWebBridgeModule(webInstance.getInstanceId());
        setWebViewClient(new CmlWebViewClient(webBridgeModule));
        webInstance.setWebView(this);
    }
}
