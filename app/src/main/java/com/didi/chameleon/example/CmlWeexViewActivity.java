package com.didi.chameleon.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.didi.chameleon.sdk.container.ICmlView;
import com.didi.chameleon.sdk.module.CmlCallback;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.weex.container.CmlWeexView;

import org.json.JSONException;
import org.json.JSONObject;

public class CmlWeexViewActivity extends AppCompatActivity implements View.OnClickListener, CmlWeexView.IDegradeToH5 {
    private static final String TAG = "CmlWeexViewActivity";

    private static final String WEB_URL = "http://jalon.wang/cml-demo-say/dist/web/cml-demo-say.html";
    private static final String URL_JS_BUNDLE = WEB_URL + "?cml_addr=http%3a%2f%2fjalon.wang%2fcml-demo-say%2fdist%2fweex%2fcml-demo-say_0c731e1c5e428213d27a.js";

    private TextView txtChangeTxt;
    private CmlWeexView cmlWeexView;
    private ICmlView cmlView;

    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cml_weex_view);

        txtChangeTxt = findViewById(R.id.txt_change_txt);
        cmlView = cmlWeexView = findViewById(R.id.cml_view);

        cmlWeexView.setDegradeToH5(this);
        cmlWeexView.onCreate();
        cmlWeexView.render(URL_JS_BUNDLE, null); // 加载远程jsbundle
//        cmlWeexView.render("file://local/cml-demo-say.js", null); // 加载assets目录里的jsbundle

        txtChangeTxt.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cmlWeexView != null) {
            cmlWeexView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cmlWeexView != null) {
            cmlWeexView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cmlWeexView != null) {
            cmlWeexView.onDestroy();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_change_txt:
                cmlView.invokeJsMethod("moduleDemo", "NaTellJS", getContent(),
                        new CmlCallback<String>(String.class) {
                            @Override
                            public void onCallback(@Nullable String data) {
                                CmlLogUtil.d(TAG, "data: " + data);
                            }
                        });
                break;
        }
    }

    @Override
    public void setView(ICmlView cmlView) {
        this.cmlView = cmlView;
    }

    /**
     * 这个方法只是为了产生一个json串，界面在每次点击时发生变化
     *
     * @return 返回一个json串，例 {"content":"测试1"}
     */
    private String getContent() {
        String content = "测试" + num++;

        JSONObject obj = new JSONObject();
        try {
            obj.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
