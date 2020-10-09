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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
    public void setImage(ICmlInstance instance, String url, final ImageView view, final OnLoadCallback callback) {
        checkGlide();
        RequestBuilder<Drawable> typeRequest = getCommonStep(url, view.getContext());
        if (typeRequest == null) {
            if (callback != null) {
                callback.onFinish(false);
            }
            return;
        }
        typeRequest.transition(new DrawableTransitionOptions().crossFade())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (callback != null) {
                            callback.onFinish(false);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (callback != null) {
                            callback.onFinish(true);
                        }
                        return false;
                    }
                })
                .into(view);
    }

    private boolean activityValid(Activity activity) {
        return !(activity == null || activity.isFinishing()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()));
    }
}
