package com.didi.chameleon.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.bundle.CmlBundle;
import com.didi.chameleon.sdk.utils.Util;

import java.util.ArrayList;
import java.util.List;

import cn.zkml.healthmanagement.member.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    // 演示打开一般的URL
    private static final String WEB_URL = "http://jalon.wang/cml-demo-say/dist/web/cml-demo-say.html";
    // 这是一个可以正常打开的 JS_BUNDLE
    private static final String URL_JS_BUNDLE_OK = WEB_URL + "?cml_addr=http%3a%2f%2fjalon.wang%2fcml-demo-say%2fdist%2fweex%2fcml-demo-say_0c731e1c5e428213d27a.js";
    // 这是一个错误的 JS_BUNDLE
    private static final String URL_JS_BUNDLE_ERR = WEB_URL + "?cml_addr=not-exist-bundle.js";
    // 这是一个测试预加载的 JS_BUNDLE
    private static final String URL_JS_BUNDLE_PRELOAD = WEB_URL + "?cml_addr=http%3a%2f%2fjalon.wang%2fcml-demo-say%2fdist%2fweex%2fcml-demo-say_0c731e1c5e428213d27a.js";
    // 演示自定义Module 和 JS 通信, 加载本地jsbundle需设置 CmlEnvironment.CML_DEBUG = true
//    private static final String URL_MODULE_DEMO = "file://local/cml-demo-say.js";
    private static final String URL_MODULE_DEMO = "file://local/test.js";
    private static final String CML_SAY = "file://local/cml-demo-say-master-2cedd22f5158ad13251a935a96646c89f9f61948_f18670d849d1fb47abf4.js";
    private static final String CML_DEMO = "file://local/cml-demo.js";

    private TextView txtOpenUrlInActivity;
    private TextView txtOpenUrlInView;
    private TextView txtOpenCanvas;
    private TextView txtPreload;
    private TextView txtDegrade;
    private TextView txtJsCallNative;
    private TextView txtNativeCallJs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtOpenUrlInActivity = findViewById(R.id.txt_web_activity);
        txtOpenUrlInView = findViewById(R.id.txt_web_view);
        txtOpenCanvas = findViewById(R.id.txt_open_canvas);
        txtPreload = findViewById(R.id.txt_preload);
        txtDegrade = findViewById(R.id.txt_auto_degrade);
        txtJsCallNative = findViewById(R.id.txt_weex_activity);
        txtNativeCallJs = findViewById(R.id.txt_weex_view);
        txtOpenUrlInActivity.setOnClickListener(this);
        txtOpenUrlInView.setOnClickListener(this);
        txtOpenCanvas.setOnClickListener(this);
        txtPreload.setOnClickListener(this);
        txtDegrade.setOnClickListener(this);
        txtJsCallNative.setOnClickListener(this);
        txtNativeCallJs.setOnClickListener(this);

        // 在业务层设置预加载地址
        CmlEngine.getInstance().initPreloadList(getPreloadList());
        // 执行预加载
        CmlEngine.getInstance().performPreload();

        CmlEngine.getInstance().launchPage(this, CML_DEMO, null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_web_activity: //全屏Web组件
                CmlEngine.getInstance().launchPage(this, WEB_URL, null);
                break;
            case R.id.txt_web_view: //Web视图组件
                startActivity(new Intent(this, CmlWebViewActivity.class));
                break;
            case R.id.txt_open_canvas:
                CmlEngine.getInstance().launchPage(this, CML_DEMO, null);
                break;
            case R.id.txt_preload: //预加载
                CmlEngine.getInstance().launchPage(this, URL_JS_BUNDLE_PRELOAD, null);
                break;
            case R.id.txt_auto_degrade: //自动降级
                CmlEngine.getInstance().launchPage(this, URL_JS_BUNDLE_ERR, null);
                break;
            case R.id.txt_weex_activity: //全屏weex组件
                CmlEngine.getInstance().launchPage(this, URL_JS_BUNDLE_OK, null);
                break;
            case R.id.txt_weex_view: //weex视图组件
                startActivity(new Intent(this, CmlWeexViewActivity.class));
                break;
        }
    }

    private List<CmlBundle> getPreloadList() {
        List<CmlBundle> cmlModels = new ArrayList<>();
        CmlBundle model = new CmlBundle();
        model.bundle = Util.parseCmlUrl(URL_JS_BUNDLE_PRELOAD);
        model.priority = 2;
        cmlModels.add(model);
        return cmlModels;
    }
}
