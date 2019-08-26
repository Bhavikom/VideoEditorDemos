// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.seles.SelesContext;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;

public interface SelesBaseCameraInterface
{
    boolean isCapturing();
    
    boolean isCapturePaused();
    
    ImageOrientation capturePhotoOrientation();
    
    long getLastFocusTime();
    
    TuSdkSize getOutputSize();
    
    boolean isFrontFacingCameraPresent();
    
    boolean isBackFacingCameraPresent();
    
    void captureImage();
    
    void startCameraCapture();
    
    void stopCameraCapture();
    
    void pauseCameraCapture();
    
    void resumeCameraCapture();
    
    void destroy();
    
    void rotateCamera();
    
    void setFlashMode(final CameraConfigs.CameraFlash p0);
    
    CameraConfigs.CameraFlash getFlashMode();
    
    void setAntibandingMode(final CameraConfigs.CameraAntibanding p0);
    
    CameraConfigs.CameraAntibanding getAntiBandingMode();
    
    boolean canSupportFlash();
    
    void setOutputSize(final TuSdkSize p0);
    
    void setPreviewMaxSize(final int p0);
    
    void setPreviewEffectScale(final float p0);
    
    void setUnifiedParameters(final boolean p0);
    
    void setDisableMirrorFrontFacing(final boolean p0);
    
    void setEnableFaceTrace(final boolean p0);
    
    void autoMetering(final PointF p0);
    
    boolean canSupportAutoFocus();
    
    void cancelAutoFocus();
    
    void autoFocus(final CameraConfigs.CameraAutoFocus p0, final PointF p1, final TuSdkAutoFocusCallback p2);
    
    void autoFocus(final TuSdkAutoFocusCallback p0);
    
    void addTarget(final SelesContext.SelesInput p0);
    
    void addTarget(final SelesContext.SelesInput p0, final int p1);
    
    void removeTarget(final SelesContext.SelesInput p0);
    
    public interface TuSdkAutoFocusCallback
    {
        void onAutoFocus(final boolean p0, final SelesBaseCameraInterface p1);
    }
}
