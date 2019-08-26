// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine;

//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.core.struct.TuSdkSize;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

public interface TuSdkEngineOrientation
{
    void release();
    
    void setInputSize(final int p0, final int p1);
    
    TuSdkSize getInputSize();
    
    CameraConfigs.CameraFacing getCameraFacing();
    
    void setCameraFacing(final CameraConfigs.CameraFacing p0);
    
    void switchCameraFacing();
    
    InterfaceOrientation getInterfaceOrientation();
    
    void setInterfaceOrientation(final InterfaceOrientation p0);
    
    void setInputOrientation(final ImageOrientation p0);
    
    void setOutputOrientation(final ImageOrientation p0);
    
    void setHorizontallyMirrorFrontFacingCamera(final boolean p0);
    
    void setHorizontallyMirrorRearFacingCamera(final boolean p0);
    
    ImageOrientation getInputRotation();
    
    ImageOrientation getOutputOrientation();
    
    TuSdkSize getOutputSize();
    
    float getDeviceAngle();
    
    void setDeviceAngle(final float p0);
    
    InterfaceOrientation getDeviceOrient();
    
    void setDeviceOrient(final InterfaceOrientation p0);
    
    boolean isOriginalCaptureOrientation();
    
    void setOriginalCaptureOrientation(final boolean p0);
    
    boolean isOutputCaptureMirrorEnabled();
    
    void setOutputCaptureMirrorEnabled(final boolean p0);
    
    ImageOrientation getYuvOutputOrienation();
    
    void setYuvOutputOrienation(final ImageOrientation p0);
}
