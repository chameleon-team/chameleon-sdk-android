package cn.zkml.healthmanagement.member;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.CmlInstanceManage;
import com.didi.chameleon.sdk.ICmlInstance;
import com.didi.chameleon.sdk.ICmlLaunchCallback;
import com.didi.chameleon.sdk.container.CmlContainerActivity;
import com.didi.chameleon.sdk.container.ICmlActivity;
import com.didi.chameleon.sdk.widget.CmlTitleView;
import com.didi.chameleon.weex.CmlWeexInstance;

import com.taobao.weex.WXSDKEngine;

import java.util.HashMap;

public class WeexMainActivity extends CmlContainerActivity implements CmlWeexInstance.ICmlInstanceListener, ICmlActivity {
    private static final String TAG = "CmlWeexActivity";
    private CmlWeexInstance mWXInstance;

    private View loadingView;
    private CmlTitleView titleView;
    private View objectView;
    private ViewGroup viewContainer;
    /*
     * 判断view是否有效，我们认为View在onCreate~onDestroy之间为有效
     */
    private boolean mIsViewValid;
    private HashMap<String, Object> options;

    private static final String CML_DEMO = "file://local/cml-demo.js";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String instanceId = getIntent().getStringExtra(PARAM_INSTANCE_ID);
        final int requestCode = getIntent().getIntExtra(PARAM_REQUEST_CODE, -1);
        mWXInstance = new CmlWeexInstance(this, this, instanceId, requestCode);
        mWXInstance.onCreate();
        mIsViewValid = true;
        setContentView(com.didi.chameleon.weex.R.layout.cml_container_activity);
        initView();

        sdcardPermissionCheck(this);

        if (mWXInstance != null) {
            Log.e("cml", "mWXInstance != null");
            if (WXSDKEngine.isInitialized()) {
                Log.e(TAG, "init-->1");
                mWXInstance.renderByUrl(CML_DEMO, null);
            } else {
                Log.e(TAG, "init-->2");
                final Handler handler = new Handler();
                final long[] delayed = new long[1];
                delayed[0] = 100;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (WXSDKEngine.isInitialized()) {
                            Log.e(TAG, "init-->3:" +  delayed[0]);
                            mWXInstance.renderByUrl(CML_DEMO, null);
                        } else {
                            delayed[0] += 100;
                            handler.postDelayed(this, 100);
                        }
                    }
                }, 100);
            }

        }
    }

    private void initView() {
        titleView = findViewById(com.didi.chameleon.weex.R.id.cml_weex_title_bar);
        loadingView = findViewById(com.didi.chameleon.weex.R.id.cml_weex_loading_layout);
        viewContainer = findViewById(com.didi.chameleon.weex.R.id.cml_weex_content);
        titleView.showLeftBackDrawable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void renderByUrl() {
//        Intent intent = getIntent();
//        String url = intent.getStringExtra(PARAM_URL);
//        if (TextUtils.isEmpty(url)) {
//            CmlLogUtil.e(TAG, "url cant be null !");
//            return;
//        }
//        if (mWXInstance != null) {
//            options = (HashMap<String, Object>) intent.getSerializableExtra(PARAM_OPTIONS);
//            mWXInstance.renderByUrl(url, options);
//        }
    }

    @Override
    public String getInstanceId() {
        if (null != mWXInstance) {
            return mWXInstance.getInstanceId();
        }
        return null;
    }

    @Override
    public void onRenderSuccess() {
        loadingView.setVisibility(View.GONE);
        titleView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWXInstance != null) {
            mWXInstance.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWXInstance != null) {
            mWXInstance.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWXInstance != null) {
            mWXInstance.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsViewValid = false;
        if (mWXInstance != null) {
            mWXInstance.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (mWXInstance == null || !mWXInstance.onBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onDegradeToH5(String url, int degradeCode) {
        if (CmlEnvironment.getDegradeAdapter() != null) {
            CmlEnvironment.getDegradeAdapter().degradeActivity(this, url, this.options, degradeCode);
        }
        finish();
    }

    @Override
    public void onViewCreate(View view) {
        objectView = view;
        viewContainer.addView(view);
    }

    @Override
    public void updateNaviTitle(String title) {
        // do nothing, web only need to update title
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Nullable
    @Override
    public View getObjectView() {
        return objectView;
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

        public WeexMainActivity.Launch addOptions(HashMap<String, Object> options) {
            this.options = options;
            return this;
        }

        public WeexMainActivity.Launch addRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public WeexMainActivity.Launch addLaunchCallback(ICmlLaunchCallback launchCallback) {
            this.launchCallback = launchCallback;
            return this;
        }

        private Intent buildIntent(String instanceId) {
            Intent intent = new Intent(activity, com.didi.chameleon.weex.container.CmlWeexActivity.class);
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
