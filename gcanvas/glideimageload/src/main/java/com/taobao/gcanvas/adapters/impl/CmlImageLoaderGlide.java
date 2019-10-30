package com.taobao.gcanvas.adapters.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.taobao.gcanvas.adapters.img.IGImageLoader;

import java.util.HashMap;

public class CmlImageLoaderGlide implements IGImageLoader {
    private static final String TAG = "CmlImageLoaderGlide";

    private Handler mMainHandler = null;

    private static HashMap<String, SimpleTarget> sTargetTrigger = new HashMap<>();

    private ImageTarget mCurrentTarget;

    @Override
    public void load(final Context context, final String url, final IGImageLoader.ImageCallback callback) {
        mCurrentTarget = new ImageTarget(url, callback);
        sTargetTrigger.put(url, mCurrentTarget);
        if (Looper.myLooper() != Looper.getMainLooper()) {
            if (null == mMainHandler) {
                mMainHandler = new Handler(Looper.getMainLooper());
            }

            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "CmlImageLoaderGlide load: " + url);
                    if (isValidContextForGlide(context)) {
                        Glide.with(context).asBitmap().load(url).into(mCurrentTarget);
                    }
                }
            });
        } else {
            Glide.with(context).asBitmap().load(url).into(mCurrentTarget);
        }
    }

    private boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    private class ImageTarget extends SimpleTarget<Bitmap> {
        private String mUrl;
        private IGImageLoader.ImageCallback mCallback;
        private Bitmap mBitmap;

        public ImageTarget(String url, IGImageLoader.ImageCallback callback) {
            this.mUrl = url;
            this.mCallback = callback;
        }

        @Override
        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
            mBitmap = bitmap;
            mCallback.onSuccess(mBitmap);
            mCallback = null;
            mBitmap = null;
            sTargetTrigger.remove(mUrl);
            mUrl = null;
        }

        @Override
        public void onLoadFailed(Drawable errorDrawable) {
            mCallback.onFail(null);
            mCallback = null;
            mBitmap = null;
            sTargetTrigger.remove(mUrl);
            mUrl = null;
        }
    }
}
