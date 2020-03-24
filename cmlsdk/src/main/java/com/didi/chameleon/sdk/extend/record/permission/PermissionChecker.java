package com.didi.chameleon.sdk.extend.record.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.HashMap;

/**
 * Created by Acathur on 17/2/16.
 *  Copyright (c) 2017 Instapp. All rights reserved.
 */

public class PermissionChecker {

    public static boolean lacksPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false;
        for (String permission : permissions) {
            if (lacksPermission(context, permission)) {
                return true;
            }
        }
        return false;
    }

    public static boolean lacksPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        } else {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED;
        }
    }

    public static boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissions(final Activity activity, HashMap<String, String> dialogParams, final ModuleResultListener listener, final int requestCode, final String... permissions) {
        if (shouldShowRequestPermissionsRationale(activity, permissions)) {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle(dialogParams.get("title"))
                    .setMessage(dialogParams.get("message"))
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            listener.onResult(true);
                        }
                    })
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestPermissionInner(activity, requestCode, permissions);
                        }
                    })
                    .create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            requestPermissionInner(activity, requestCode, permissions);
        }
    }

    public static void requestPermissionInner(Activity context, int requestCode, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(permissions, requestCode);
        }
    }

    public static boolean shouldShowRequestPermissionsRationale(Activity activity, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        } else {
            for (String permission : permissions) {
                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        } else {
            return activity.shouldShowRequestPermissionRationale(permission);
        }
    }
}
