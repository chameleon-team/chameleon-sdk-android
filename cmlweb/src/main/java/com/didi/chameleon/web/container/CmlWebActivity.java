package com.didi.chameleon.web.container;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.ICmlLaunchCallback;
import com.didi.chameleon.sdk.container.CmlContainerActivity;
import com.didi.chameleon.sdk.container.ICmlActivity;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.sdk.widget.CmlTitleView;
import com.didi.chameleon.web.CmlWebInstance;
import com.didi.chameleon.web.R;
import com.didi.chameleon.web.bridge.CmlWebView;

import java.util.HashMap;

public class CmlWebActivity extends CmlContainerActivity implements ICmlActivity {
    private static final String TAG = "CmlActivity";
    private CmlWebView mWebView;
    private CmlWebInstance mWebInstance;
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

        final String instanceId = getIntent().getStringExtra(PARAM_INSTANCE_ID);
        final int requestCode = getIntent().getIntExtra(PARAM_REQUEST_CODE, -1);
        mWebInstance = new CmlWebInstance(this, instanceId, requestCode);
        mWebInstance.onCreate();
        mIsViewValid = true;
        setContentView(R.layout.cml_container_activity);
        initView();
        renderByUrl();
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

    @Override
    public void renderByUrl() {
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
    public String getInstanceId() {
        if (null != mWebInstance) {
            return mWebInstance.getInstanceId();
        }
        return null;
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
    public void updateNaviTitle(String title) {
        if (null != titleView) {
            titleView.setTitle(title);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Nullable
    @Override
    public View getObjectView() {
        return mWebView;
    }

    @Override
    public Activity getActivity() {
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
        private Activity activity;
        private HashMap<String, Object> options;
        private int requestCode;
        private ICmlLaunchCallback launchCallback;

        public Launch(Activity activity, String url) {
            this.url = url;
            this.activity = activity;
        }

        public CmlWebActivity.Launch addOptions(HashMap<String, Object> options) {
            this.options = options;
            return this;
        }

        public CmlWebActivity.Launch addRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public CmlWebActivity.Launch addLaunchCallback(ICmlLaunchCallback launchCallback) {
            this.launchCallback = launchCallback;
            return this;
        }

        private Intent buildIntent(String instanceId) {
            Intent intent = new Intent(activity, CmlWebActivity.class);
            intent.putExtra(PARAM_URL, url);
            intent.putExtra(PARAM_REQUEST_CODE, requestCode);
            intent.putExtra(PARAM_INSTANCE_ID, instanceId);
            if (options != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(PARAM_OPTIONS, options);
                intent.putExtras(bundle);
            }
            return intent;
        }

        public void launch() {
            final String instanceId = CmlEngine.getInstance().generateInstanceId();
            activity.startActivity(buildIntent(instanceId));
        }

        public void launchForResult() {
            final String instanceId = CmlEngine.getInstance().generateInstanceId();
            if (null != launchCallback) {
                // 注册到管理类
                CmlInstanceManage.getInstance().addLaunchCallback(instanceId, new ICmlLaunchCallback() {
                    @Override
                    public void onResult(@NonNull ICmlInstance cmlInstance, int requestCode, int resultCode, String result) {
                        launchCallback.onResult(cmlInstance, requestCode, resultCode, result);
                    }

                    @Override
                    public void onCreate() {
                        launchCallback.onCreate();
                    }

                    @Override
                    public void onResume() {
                        launchCallback.onResume();
                    }

                    @Override
                    public void onPause() {
                        launchCallback.onPause();
                    }

                    @Override
                    public void onStop() {
                        launchCallback.onStop();
                    }

                    @Override
                    public void onDestroy() {
                        CmlInstanceManage.getInstance().removeLaunchCallback(instanceId);
                        launchCallback.onDestroy();
                    }
                });
            }
            activity.startActivity(buildIntent(instanceId));
        }
    }
}
