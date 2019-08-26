// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.activity;

//import org.lasque.tusdk.core.utils.TLog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkViewHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.ComponentErrorType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.TuSdkComponentErrorListener;

//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.modules.components.ComponentErrorType;
//import org.lasque.tusdk.core.TuSdkResult;
//import org.lasque.tusdk.core.view.TuSdkViewHelper;
//import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;

public abstract class TuComponentFragment extends TuFragment
{
    private TuSdkComponentErrorListener a;
    protected TuSdkViewHelper.AlertDelegate permissionAlertDelegate;
    
    public TuComponentFragment() {
        this.permissionAlertDelegate = new TuSdkViewHelper.AlertDelegate() {
            @SuppressLint("WrongConstant")
            @Override
            public void onAlertConfirm(final AlertDialog alertDialog) {
                final Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", TuComponentFragment.this.getContext().getPackageName(), (String)null));
                intent.addFlags(268435456);
                TuComponentFragment.this.startActivity(intent);
            }
            
            @Override
            public void onAlertCancel(final AlertDialog alertDialog) {
                TuComponentFragment.this.dismissActivityWithAnim();
            }
        };
    }
    
    public TuSdkComponentErrorListener getErrorListener() {
        return this.a;
    }
    
    public void setErrorListener(final TuSdkComponentErrorListener a) {
        this.a = a;
    }
    
    protected void notifyError(final TuSdkResult tuSdkResult, final ComponentErrorType componentErrorType) {
        if (componentErrorType == null || this.getErrorListener() == null) {
            return;
        }
        if (!ThreadHelper.isMainThread()) {
            new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
                @Override
                public void run() {
                    TuComponentFragment.this.notifyError(tuSdkResult, componentErrorType);
                }
            });
            return;
        }
        this.getErrorListener().onComponentError(this, tuSdkResult, componentErrorType.getError(this));
    }
    
    @TargetApi(23)
    protected String[] getRequiredPermissions() {
        return new String[] { "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_NETWORK_STATE", "android.permission.ACCESS_FINE_LOCATION" };
    }
    
    public int getRequestPermissionCode() {
        return 1;
    }
    
    @TargetApi(23)
    public boolean hasRequiredPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        final String[] requiredPermissions = this.getRequiredPermissions();
        if (requiredPermissions != null && requiredPermissions.length > 0) {
            for (final String s : requiredPermissions) {
                if (this.getActivity().checkSelfPermission(s) != PackageManager.PERMISSION_GRANTED) {
                    if (s != "android.permission.ACCESS_FINE_LOCATION") {
                        return false;
                    }
                    TLog.d("Access to fine location has been denied", new Object[0]);
                }
            }
        }
        return true;
    }
    
    public void requestRequiredPermissions() {
        final String[] requiredPermissions = this.getRequiredPermissions();
        if (requiredPermissions != null && requiredPermissions.length > 0) {
            this.requestPermissions(requiredPermissions, this.getRequestPermissionCode());
        }
    }
    
    public void onRequestPermissionsResult(final int n, final String[] array, final int[] array2) {
        super.onRequestPermissionsResult(n, array, array2);
        if (n == this.getRequestPermissionCode()) {
            for (int length = array2.length, n2 = 0; n2 < length && array2[n2] == 0; ++n2) {}
            this.onPermissionGrantedResult(this.hasRequiredPermission());
        }
    }
    
    protected void onPermissionGrantedResult(final boolean b) {
    }
}
