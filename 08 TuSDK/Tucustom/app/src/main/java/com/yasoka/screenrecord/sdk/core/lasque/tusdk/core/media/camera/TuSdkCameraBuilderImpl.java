// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

import java.io.IOException;
import android.graphics.SurfaceTexture;
//import org.lasque.tusdk.core.utils.hardware.CameraHelper;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
import android.hardware.Camera;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraHelper;

public class TuSdkCameraBuilderImpl implements TuSdkCameraBuilder
{
    private Camera.CameraInfo a;
    private Camera b;
    private CameraConfigs.CameraFacing c;
    
    public TuSdkCameraBuilderImpl() {
        this.c = CameraConfigs.CameraFacing.Back;
    }
    
    @Override
    public Camera.CameraInfo getInfo() {
        return this.a;
    }
    
    @Override
    public Camera getOrginCamera() {
        return this.b;
    }
    
    @Override
    public Camera.Parameters getParameters() {
        if (this.b == null) {
            return null;
        }
        return this.b.getParameters();
    }
    
    @Override
    public CameraConfigs.CameraFacing getFacing() {
        return this.c;
    }
    
    @Override
    public boolean isBackFacingCameraPresent() {
        return this.c == CameraConfigs.CameraFacing.Back;
    }

    @Override
    public boolean open(CameraConfigs.CameraFacing c) {
        if (c == null) {
            TLog.e("%s open need a CameraFacing", "TuSdkCameraBuilderImpl");
            return false;
        }
        this.c = c;
        return this.open();
    }

    
    @Override
    public boolean open() {
        this.releaseCamera();
        this.a = CameraHelper.firstCameraInfo(this.c);
        if (this.a == null) {
            TLog.e("%s open can not find any camera info: %s", "TuSdkCameraBuilderImpl", this.c);
            return false;
        }
        this.c = CameraHelper.getCameraFacing(this.a);
        this.b = CameraHelper.getCamera(this.a);
        if (this.b == null) {
            TLog.e("%s open can not find any camera: %s", "TuSdkCameraBuilderImpl", this.a);
            return false;
        }
        return true;
    }
    
    @Override
    public void releaseCamera() {
        if (this.b == null) {
            return;
        }
        try {
            this.b.setPreviewCallback((Camera.PreviewCallback)null);
            this.b.cancelAutoFocus();
            this.b.stopPreview();
            this.b.release();
        }
        catch (Exception ex) {
            TLog.e(ex, "%s releaseCamera has error, ignore.", "TuSdkCameraBuilderImpl");
        }
        finally {
            this.b = null;
        }
    }
    
    @Override
    public boolean startPreview() {
        if (this.b == null) {
            TLog.w("%s startPreview need after open.", "TuSdkCameraBuilderImpl");
            return false;
        }
        try {
            this.b.startPreview();
        }
        catch (Exception ex) {
            TLog.e(ex, "%s startPreview has error.", "TuSdkCameraBuilderImpl");
        }
        return true;
    }
    
    @Override
    public void setPreviewTexture(final SurfaceTexture previewTexture) {
        if (this.b == null) {
            return;
        }
        try {
            this.b.setPreviewTexture(previewTexture);
        }
        catch (IOException ex) {
            TLog.e(ex, "%s setPreviewTexture failed.", "TuSdkCameraBuilderImpl");
        }
    }
    
    @Override
    public void setPreviewCallbackWithBuffer(final Camera.PreviewCallback previewCallbackWithBuffer) {
        if (this.b == null) {
            return;
        }
        this.b.setPreviewCallbackWithBuffer(previewCallbackWithBuffer);
    }
    
    @Override
    public void addCallbackBuffer(final byte[] array) {
        if (this.b == null) {
            return;
        }
        this.b.addCallbackBuffer(array);
    }
}
