// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.TuSdkContext;
import android.hardware.Camera;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraHelper;
//import org.lasque.tusdk.core.utils.hardware.CameraHelper;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;

public class TuSdkCameraParametersImpl implements TuSdkCameraParameters
{
    private boolean a;
    private CameraConfigs.CameraAntibanding b;
    private CameraConfigs.CameraWhiteBalance c;
    private CameraConfigs.CameraFlash d;
    private TuSdkCameraBuilder e;
    
    public TuSdkCameraParametersImpl() {
        this.a = false;
    }
    
    public boolean isUnifiedParameters() {
        return this.a;
    }
    
    public void setUnifiedParameters(final boolean a) {
        this.a = a;
    }
    
    public void setAntibandingMode(final CameraConfigs.CameraAntibanding b) {
        this.b = b;
        final Camera.Parameters a = this.a();
        if (a == null) {
            return;
        }
        CameraHelper.setAntibanding(a, this.b);
        this.a(a);
    }
    
    public CameraConfigs.CameraAntibanding getAntiBandingMode() {
        final Camera.Parameters a = this.a();
        if (a == null) {
            return this.b;
        }
        return CameraHelper.antiBandingType(a.getAntibanding());
    }
    
    public void setWhiteBalance(final CameraConfigs.CameraWhiteBalance c) {
        this.c = c;
        final Camera.Parameters a = this.a();
        if (a == null) {
            return;
        }
        CameraHelper.setWhiteBalance(a, this.c);
        this.a(a);
    }
    
    public CameraConfigs.CameraWhiteBalance getWhiteBalance() {
        final Camera.Parameters a = this.a();
        if (a == null) {
            return this.c;
        }
        return CameraHelper.whiteBalance(a.getAntibanding());
    }
    
    public CameraConfigs.CameraFlash getFlashMode() {
        final Camera.Parameters a = this.a();
        if (a == null) {
            return CameraConfigs.CameraFlash.Off;
        }
        return CameraHelper.getFlashMode(a);
    }
    
    @Override
    public void setFlashMode(final CameraConfigs.CameraFlash d) {
        if (d == null) {
            return;
        }
        this.d = d;
        if (!CameraHelper.canSupportFlash(TuSdkContext.context())) {
            return;
        }
        final Camera.Parameters a = this.a();
        if (a == null) {
            return;
        }
        CameraHelper.setFlashMode(a, d);
        this.a(a);
    }
    
    public boolean canSupportFlash() {
        return CameraHelper.canSupportFlash(TuSdkContext.context()) && CameraHelper.supportFlash(this.a());
    }
    
    @Override
    public void configure(final TuSdkCameraBuilder e) {
        if (e == null) {
            TLog.e("%s configure builder is empty.", "TuSdkCameraParametersImpl");
            return;
        }
        this.e = e;
        final Camera.Parameters a = this.a();
        if (a == null) {
            TLog.e("%s configure Camera.Parameters is empty.", "TuSdkCameraParametersImpl");
            return;
        }
        if (this.isUnifiedParameters()) {
            CameraHelper.unifiedParameters(a);
        }
        CameraHelper.setFlashMode(a, this.getFlashMode());
        CameraHelper.setAntibanding(a, this.getAntiBandingMode());
        CameraHelper.setWhiteBalance(a, this.getWhiteBalance());
        this.a(a);
    }
    
    @Override
    public void changeStatus(final TuSdkCamera.TuSdkCameraStatus tuSdkCameraStatus) {
    }
    
    private Camera.Parameters a() {
        if (this.e == null) {
            return null;
        }
        return this.e.getParameters();
    }
    
    private void a(final Camera.Parameters parameters) {
        if (this.e == null || this.e.getOrginCamera() == null) {
            return;
        }
        this.e.getOrginCamera().setParameters(parameters);
    }
    
    public void logParameters() {
        CameraHelper.logParameters(this.a());
    }
}
