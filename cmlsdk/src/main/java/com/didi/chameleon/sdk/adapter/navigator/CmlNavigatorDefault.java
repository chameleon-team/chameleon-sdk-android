package com.didi.chameleon.sdk.adapter.navigator;

import android.app.Activity;
import android.content.Context;

import com.didi.chameleon.sdk.CmlEngine;
import com.didi.chameleon.sdk.utils.CmlLogUtil;

public class CmlNavigatorDefault implements ICmlNavigatorAdapter {

    private static final String TAG = "CmlNavigatorDefault";

    @Override
    public void navigator(Context context, String url) {
        if (context instanceof Activity) {
            CmlEngine.getInstance().launchPage((Activity) context, url, null);
        } else {
            CmlLogUtil.w(TAG, "navigator fail by content type");
        }
    }

}
