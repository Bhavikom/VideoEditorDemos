// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.SurfaceTexture;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;

public interface SelesVideoCamera2Engine
{
    boolean canInitCamera();
    
    boolean onInitCamera();
    
    TuSdkSize previewOptimalSize();
    
    void onCameraWillOpen(final SurfaceTexture p0);
    
    void onCameraStarted();
    
    ImageOrientation previewOrientation();
}
