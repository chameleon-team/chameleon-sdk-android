package com.didi.chameleon.rn.container;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.didi.chameleon.rn.CmlRnInstance;
import com.didi.chameleon.rn.R;
import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.container.CmlContainerActivity;
import com.didi.chameleon.sdk.container.ICmlActivity;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.sdk.widget.CmlTitleView;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;

import java.util.HashMap;

public class CmlRnActivity extends CmlContainerActivity implements CmlRnInstance.ICmlInstanceListener, ICmlActivity {

    private static final String TAG = "CmlActivity";
    private ReactRootView mReactRootView;
    private CmlRnInstance mRnInstance;
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
        mRnInstance = new CmlRnInstance(this, this, this);
        mRnInstance.onCreate();
        mIsViewValid = true;
        setContentView(R.layout.cml_container_activity);
        initView();
        mReactRootView = new ReactRootView(this);
        viewContainer.addView(mReactRootView);

        sdcardPermissionCheck(this);
    }

    private void initView() {
        titleView = findViewById(R.id.cml_weex_title_bar);
        loadingView = findViewById(R.id.cml_weex_loading_layout);
        loadingView.setVisibility(View.GONE); // 临时隐藏
        viewContainer = findViewById(R.id.cml_weex_content);
        titleView.showLeftBackDrawable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void renderByUrl() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(PARAM_URL);
        if (TextUtils.isEmpty(url)) {
            CmlLogUtil.e(TAG, "url cant be null !");
            return;
        }
        if (mRnInstance != null) {
            options = (HashMap<String, Object>) intent.getSerializableExtra(PARAM_OPTIONS);
            mRnInstance.renderByUrl(url, options);
        }
    }

    @Override
    public void onReactInstanceCreated(ReactInstanceManager reactInstance) {
        if (null != reactInstance) {
            mReactRootView.startReactApplication(reactInstance, "HelloWorld", null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRnInstance != null) {
            mRnInstance.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRnInstance != null) {
            mRnInstance.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRnInstance != null) {
            mRnInstance.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsViewValid = false;
        if (mRnInstance != null) {
            mRnInstance.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDegradeToH5(String url, int degradeCode) {
        if (CmlEnvironment.getDegradeAdapter() != null) {
            CmlEnvironment.getDegradeAdapter().degradeActivity(this, url, this.options, degradeCode);
        }
        finish();
    }

    @Override
    public void updateNaviTitle(String title) {
        // do nothing, web only need to update title
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

        public CmlRnActivity.Launch addOptions(HashMap<String, Object> options) {
            this.options = options;
            return this;
        }

        private Intent buildIntent() {
            Intent intent = new Intent(context, CmlRnActivity.class);
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
