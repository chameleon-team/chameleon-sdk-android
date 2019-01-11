package com.didi.chameleon.sdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Chameleon 默认图片加载实现，基于 Glide 实现
 * Created by youzicong on 2018/10/11
 */
public class CmlDefaultImgLoaderAdapter implements ICmlImgLoaderAdapter {
    private static final String TAG = "CmlDefaultImgLoaderAdapter";

    @Override
    public void setImage(String url, ImageView view) {
        try {
            Class.forName("com.bumptech.glide.Glide");
        } catch (Exception e) {
            throw CmlAdapterException.throwAdapterNone(ICmlImgLoaderAdapter.class);
        }
        Context context = view.getContext();
        if (context instanceof Activity && activityValid((Activity) context)) {
            Glide.with(context).load(url).into(view);
        }
    }

    private boolean activityValid(Activity activity) {
        return !(activity == null || activity.isFinishing()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()));
    }
}
