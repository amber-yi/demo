package com.amber.livedemo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by luosiyi on 2017/6/30.
 */

public class PermissionsUtils implements
        ActivityCompat.OnRequestPermissionsResultCallback {
    private String[] permissions;

    public PermissionsUtils setPermissions(String... permission) {
        permissions = permission;
        return this;
    }

    private void startRequest(Activity activity,boolean request) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }


    interface Callback {
        void allow();

        void refuse();
    }
}
