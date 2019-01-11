package com.didi.chameleon.weex.container;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.didi.chameleon.sdk.CmlEnvironment;
import com.didi.chameleon.sdk.container.CmlContainerActivity;
import com.didi.chameleon.sdk.container.ICmlActivity;
import com.didi.chameleon.sdk.utils.CmlLogUtil;
import com.didi.chameleon.sdk.widget.CmlTitleView;
import com.didi.chameleon.weex.CmlWeexEngine;
import com.didi.chameleon.weex.CmlWeexInstance;
import com.didi.chameleon.weex.R;

import java.util.HashMap;

/**
 * 用来展示 Chameleon Weex 页面，通过{@link Launch} 或者{@link CmlWeexEngine#launchPage(Context, String, HashMap)}进行启动
 *

 * @since 18/5/24
 */
public class CmlWeexActivity extends CmlContainerActivity implements CmlWeexInstance.ICmlInstanceListener, ICmlActivity {
    private static final String TAG = "CmlWeexActivity";
    private CmlWeexInstance mWXInstance;
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
        mWXInstance = new CmlWeexInstance(this, this, this);
        mWXInstance.onCreate();
        mIsViewValid = true;
        setContentView(R.layout.cml_container_activity);
        initView();

        sdcardPermissionCheck(this);
    }

    private void initView() {
        titleView = findViewById(R.id.cml_weex_title_bar);
        loadingView = findViewById(R.id.cml_weex_loading_layout);
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
        if (mWXInstance != null) {
            options = (HashMap<String, Object>) intent.getSerializableExtra(PARAM_OPTIONS);
            mWXInstance.renderByUrl(url, options);
        }
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
    public void onViewCreate(View view) {
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

        public CmlWeexActivity.Launch addOptions(HashMap<String, Object> options) {
            this.options = options;
            return this;
        }

        private Intent buildIntent() {
            Intent intent = new Intent(context, CmlWeexActivity.class);
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
