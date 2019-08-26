// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

//import org.lasque.tusdk.core.utils.image.ImageOrientation;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

public interface TuSdkCameraOrientation
{
    void configure(final TuSdkCameraBuilder p0);
    
    void changeStatus(final TuSdkCamera.TuSdkCameraStatus p0);
    
    ImageOrientation previewOrientation();
    
    ImageOrientation captureOrientation();
}
