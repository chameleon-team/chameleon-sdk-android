package com.didi.chameleon.sdk;

import android.support.annotation.NonNull;

public interface ICmlLaunchCallback extends ICmlBaseLifecycle {
    void onResult(@NonNull ICmlInstance cmlInstance, int requestCode, int resultCode, String result);
}
