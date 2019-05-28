package com.didi.chameleon.sdk.adapter.modal;

import android.content.Context;

public interface ICmlProgressAdapter {

    void show(Context context, String title, boolean mask);

    void dismiss();

}
