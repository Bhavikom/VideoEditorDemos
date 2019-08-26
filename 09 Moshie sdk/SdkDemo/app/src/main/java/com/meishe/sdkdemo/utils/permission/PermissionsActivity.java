package com.meishe.sdkdemo.utils.permission;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

public class PermissionsActivity extends AppCompatActivity {
    public static final int PERMISSIONS_GRANTED = 0; // 权限授权
    public static final int PERMISSIONS_DENIED = 1; // 权限拒绝
    public static final int PERMISSIONS_No_PROMPT = 2; // 权限不再提示
    private static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
    public static final String EXTRA_PERMISSIONS =
            "com.meicam.sdkdemo.utils.permission.extra_permission"; // 权限参数
    private PermissionsChecker mChecker; // 权限检测器


    // 启动当前权限页面的公开接口
    public static void startActivityForResult(Activity activity, int requestCode, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!");
        }
        mChecker = new PermissionsChecker(this);
        String[] permissions = getPermissions();
        if (mChecker.lacksPermissions(permissions)) {
            requestPermissions(permissions); // 请求权限
        } else {
            allPermissionsGranted(); // 全部权限都已获取
        }
    }

    // 返回传递的权限参数
    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    // 请求权限兼容低版本
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    // 全部权限均已获取
    private void allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED);
        finish();
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int mResultCode = -1;
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                        if (showRequestPermission) {
                            mResultCode = PERMISSIONS_DENIED;
                        } else { // false 被禁止了，不在访问
                            mResultCode = PERMISSIONS_No_PROMPT;
                            break;
                        }
                    }
                }
                if (mResultCode == -1) {
                    mResultCode = PERMISSIONS_GRANTED;
                }
                setActivityResult(mResultCode);
                break;
        }

    }

    private void setActivityResult(int code) {
        setResult(code);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
