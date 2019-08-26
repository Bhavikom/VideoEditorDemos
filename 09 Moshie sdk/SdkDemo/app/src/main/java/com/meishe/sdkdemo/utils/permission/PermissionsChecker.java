package com.meishe.sdkdemo.utils.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;



public class PermissionsChecker {
    private final Context mContext;

    public PermissionsChecker(Context context) {
        mContext = context.getApplicationContext();
    }

    public List<String> checkPermission(String... permissions) {
        List<String> newList = new ArrayList<>();
        for (String permission : permissions) {
            if (lacksPermission(permission)){
                newList.add(permission);
            }
        }
        return  newList;
    }
    public List<String> checkPermission(List<String> permissions) {
        List<String> newList = new ArrayList<>();
        for (String permission : permissions) {
            if (lacksPermission(permission)){
                newList.add(permission);
            }
        }
        return  newList;
    }

    // 判断权限集合
    public boolean lacksPermissions(String... permissions) {
        for (String permission : permissions) {
            if (lacksPermission(permission)){
                return true;
            }
        }
        return false;
    }
    // 判断权限集合
    public boolean lacksPermissions(List<String> permissions) {

        for (String permission : permissions) {
            if (lacksPermission(permission)){
                return true;
            }
        }
        return false;
    }


    // 判断是否缺少权限
    private boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

}
