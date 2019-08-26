// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

import android.graphics.SurfaceTexture;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
import android.hardware.Camera;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;

public interface TuSdkCameraBuilder
{
    Camera.CameraInfo getInfo();
    
    Camera getOrginCamera();
    
    Camera.Parameters getParameters();
    
    CameraConfigs.CameraFacing getFacing();
    
    boolean isBackFacingCameraPresent();
    
    boolean open(final CameraConfigs.CameraFacing p0);
    
    boolean open();
    
    void releaseCamera();
    
    boolean startPreview();
    
    void setPreviewTexture(final SurfaceTexture p0);
    
    void setPreviewCallbackWithBuffer(final Camera.PreviewCallback p0);
    
    void addCallbackBuffer(final byte[] p0);
}
