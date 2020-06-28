package com.didi.chameleon.sdk.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.didi.chameleon.sdk.ICmlInstance;

import java.util.regex.Pattern;

/**
 * Chameleon 默认图片加载实现，基于 Glide 实现
 * Created by youzicong on 2018/10/11
 */
public class CmlDefaultImgLoaderAdapter implements ICmlImgLoaderAdapter {

    private void checkGlide() {
        try {
            Class.forName("com.bumptech.glide.Glide");
        } catch (Exception e) {
            throw CmlAdapterException.throwAdapterNone(ICmlImgLoaderAdapter.class);
        }
    }

    @Nullable
    private RequestBuilder<Drawable> getCommonStep(String url, Context context) {
        if (url == null) {
            url = "";
        }
        if (context instanceof Activity && activityValid((Activity) context)) {
            RequestBuilder<Drawable> typeRequest;
            if (Pattern.matches("^data:image/(.*?);base64,(.*?)", url)) {
                typeRequest = Glide.with(context).load(Base64.decode(url.split(",", 2)[1], Base64.DEFAULT));
            } else {
                typeRequest = Glide.with(context).load(url);
            }
            return typeRequest;
        }
        return null;
    }

    @Override
    public void setImage(ICmlInstance instance, String url, final ImageView view) {
        checkGlide();
        RequestBuilder<Drawable> typeRequest = getCommonStep(url, view.getContext());
        if (typeRequest == null) {
            return;
        }
        typeRequest.transition(new DrawableTransitionOptions().crossFade()).into(view);
    }

    private boolean activityValid(Activity activity) {
        return !(activity == null || activity.isFinishing()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()));
    }
}
