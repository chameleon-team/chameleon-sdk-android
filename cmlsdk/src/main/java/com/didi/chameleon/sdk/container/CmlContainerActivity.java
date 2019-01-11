package com.didi.chameleon.sdk.container;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

public abstract class CmlContainerActivity extends FragmentActivity {

    private static final int REQUEST_WRITE_SDCARD = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_SDCARD && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            renderByUrl();
        }
    }

    protected void sdcardPermissionCheck(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //判断是否有这个权限
            if (permission != PackageManager.PERMISSION_GRANTED) {
                //2、申请权限: 参数二：权限的数组；参数三：请求码
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_SDCARD);
            } else {
                renderByUrl();
            }
        } else {
            renderByUrl();
        }
    }

    protected abstract void renderByUrl();
}
