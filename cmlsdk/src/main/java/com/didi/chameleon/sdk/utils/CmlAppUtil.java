package com.didi.chameleon.sdk.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class CmlAppUtil {

    public interface Callback {
        void onCallback();
    }

    public static void waitNextActivityCreate(Context context, long delay, Callback callback) {
        if (callback == null) {
            return;
        }
        if (context == null) {
            callback.onCallback();
            return;
        }
        final Context appContext = context.getApplicationContext();
        if (!(appContext instanceof Application)) {
            callback.onCallback();
            return;
        }
        final Callback[] callbacks = new Callback[]{callback};
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callbacks[0] != null) {
                    callbacks[0].onCallback();
                    callbacks[0] = null;
                }
            }
        }, delay);
        ((Application) appContext).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.getApplication().unregisterActivityLifecycleCallbacks(this);
                if (callbacks[0] != null) {
                    callbacks[0].onCallback();
                    callbacks[0] = null;
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

}
