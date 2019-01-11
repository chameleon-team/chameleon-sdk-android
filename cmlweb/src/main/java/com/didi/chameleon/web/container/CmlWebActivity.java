package com.didi.chameleon.web.container;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.didi.chameleon.sdk.bridge.CmlBridgeManager;
import com.didi.chameleon.sdk.container.ICmlActivity;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.sdk.widget.CmlTitleView;
import com.didi.chameleon.web.CmlWebInstance;
import com.didi.chameleon.web.R;
import com.didi.chameleon.web.bridge.CmlWebView;

import java.util.HashMap;

public class CmlWebActivity extends FragmentActivity implements ICmlActivity {
    private static final String TAG = "CmlActivity";
    private CmlWebView mWebView;
    private CmlWebInstance mWebInstance;
    private static final String PARAM_URL = "url";
    private static final String PARAM_OPTIONS = "options";
    private View loadingView;
    private CmlTitleView titleView;
    private ViewGroup viewContainer;
    /*
     * 判断view是否有效，我们认为View在onCreate~onDestroy之间为有效
     */
    private boolean mIsViewValid;
    private HashMap<String, Object> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebInstance = new CmlWebInstance(this, this);
        mWebInstance.onCreate();
        mIsViewValid = true;
        setContentView(R.layout.cml_container_activity);
        initView();
        initIntent();
    }

    private void initView() {
        titleView = findViewById(R.id.cml_weex_title_bar);
        loadingView = findViewById(R.id.cml_weex_loading_layout);
        loadingView.setVisibility(View.GONE); // 临时隐藏
        viewContainer = findViewById(R.id.cml_weex_content);

        mWebView = new CmlWebView(this);
        viewContainer.addView(mWebView);
        if (null != mWebInstance) {
            mWebView.startApplication(mWebInstance);
        }

        titleView.showLeftBackDrawable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initIntent() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(PARAM_URL);
        if (TextUtils.isEmpty(url)) {
            CmlLogUtil.e(TAG, "url cant be null !");
            return;
        }
        if (mWebInstance != null) {
            options = (HashMap<String, Object>) intent.getSerializableExtra(PARAM_OPTIONS);
            mWebInstance.renderByUrl(url, options);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebInstance != null) {
            mWebInstance.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebInstance != null) {
            mWebInstance.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWebInstance != null) {
            mWebInstance.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsViewValid = false;
        if (mWebInstance != null) {
            mWebInstance.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updateNaviTitle(String title) {
        if (null != titleView) {
            titleView.setTitle(title);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean isActivity() {
        return true;
    }

    @Override
    public boolean isView() {
        return false;
    }

    @Override
    public boolean isInDialog() {
        return false;
    }

    @Override
    public boolean isValid() {
        return mIsViewValid;
    }

    @Override
    public void finishSelf() {
        finish();
    }

    @Override
    public void setPageResult(int resultCode, Intent data) {
        setResult(resultCode, data);
    }

    @Override
    public void overrideAnim(int enterAnim, int exitAnim) {
        overridePendingTransition(enterAnim, exitAnim);
    }

    public static final class Launch {
        private String url;
        private Context context;
        private HashMap<String, Object> options;

        public Launch(Context context, String url) {
            this.url = url;
            this.context = context;
        }

        public CmlWebActivity.Launch addOptions(HashMap<String, Object> options) {
            this.options = options;
            return this;
        }

        private Intent buildIntent() {
            Intent intent = new Intent(context, CmlWebActivity.class);
            intent.putExtra(PARAM_URL, url);
            if (options != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(PARAM_OPTIONS, options);
                intent.putExtras(bundle);
            }
            return intent;
        }

        public void launch() {
            context.startActivity(buildIntent());
        }
    }
}
