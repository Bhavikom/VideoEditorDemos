// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

import android.opengl.GLSurfaceView;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.record.TuSdkRecordSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.core.media.record.TuSdkRecordSurface;

public interface TuSdkCamera extends TuSdkRecordSurface
{
    TuSdkCameraStatus getCameraStatus();
    
    void setCameraListener(final TuSdkCameraListener p0);
    
    void setPreviewCallback(final Camera.PreviewCallback p0);
    
    void setSurfaceListener(final SurfaceTexture.OnFrameAvailableListener p0);
    
    void setCameraBuilder(final TuSdkCameraBuilder p0);
    
    void setCameraParameters(final TuSdkCameraParameters p0);
    
    void setCameraOrientation(final TuSdkCameraOrientation p0);
    
    void setCameraFocus(final TuSdkCameraFocus p0);
    
    void setCameraSize(final TuSdkCameraSize p0);
    
    void setCameraShot(final TuSdkCameraShot p0);
    
    boolean prepare();
    
    boolean rotateCamera();
    
    CameraConfigs.CameraFacing getFacing();
    
    boolean startPreview();
    
    boolean startPreview(final CameraConfigs.CameraFacing p0);
    
    boolean pausePreview();
    
    boolean resumePreview();
    
    boolean shotPhoto();
    
    void stopPreview();
    
    long newFrameReadyInGLThread();
    
    GLSurfaceView.Renderer getExtenalRenderer();
    
    void release();
    
    public interface TuSdkCameraListener
    {
        void onStatusChanged(final TuSdkCameraStatus p0, final TuSdkCamera p1);
    }
    
    public enum TuSdkCameraStatus
    {
        CAMERA_START, 
        CAMERA_START_PREVIEW, 
        CAMERA_PAUSE_PREVIEW, 
        CAMERA_PREPARE_SHOT, 
        CAMERA_SHOTED, 
        CAMERA_STOP;
    }
}
