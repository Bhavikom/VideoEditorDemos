// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;

public interface TuSdkCameraParameters
{
    void configure(final TuSdkCameraBuilder p0);
    
    void changeStatus(final TuSdkCamera.TuSdkCameraStatus p0);
    
    void setFlashMode(final CameraConfigs.CameraFlash p0);
}
