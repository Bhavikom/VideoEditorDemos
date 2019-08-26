// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.SurfaceTexture;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.hardware.Camera;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

public interface SelesVideoCameraEngine
{
    boolean canInitCamera();
    
    Camera onInitCamera();
    
    TuSdkSize previewOptimalSize();
    
    void onCameraWillOpen(final SurfaceTexture p0);
    
    void onCameraStarted();
    
    ImageOrientation previewOrientation();
}
