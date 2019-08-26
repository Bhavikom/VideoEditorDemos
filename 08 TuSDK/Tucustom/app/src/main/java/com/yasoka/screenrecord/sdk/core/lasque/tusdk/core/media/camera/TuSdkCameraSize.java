// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

//import org.lasque.tusdk.core.struct.TuSdkSize;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;

public interface TuSdkCameraSize
{
    void configure(final TuSdkCameraBuilder p0);
    
    void changeStatus(final TuSdkCamera.TuSdkCameraStatus p0);
    
    TuSdkSize previewOptimalSize();
    
    int previewBufferLength();
    
    TuSdkSize getOutputSize();
}
