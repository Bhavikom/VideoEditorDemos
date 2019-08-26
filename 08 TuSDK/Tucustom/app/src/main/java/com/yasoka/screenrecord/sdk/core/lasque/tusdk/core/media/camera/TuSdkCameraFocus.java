// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera;

//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.hardware.TuSdkFace;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkFace;

import java.util.List;

public interface TuSdkCameraFocus
{
    void configure(final TuSdkCameraBuilder p0, final TuSdkCameraOrientation p1, final TuSdkCameraSize p2);
    
    void changeStatus(final TuSdkCamera.TuSdkCameraStatus p0);
    
    boolean allowFocusToShot();
    
    void autoFocus(final TuSdkCameraFocusListener p0);
    
    public interface TuSdkCameraFocusFaceListener
    {
        void onFocusFaceDetection(final List<TuSdkFace> p0, final TuSdkSize p1);
    }
    
    public interface TuSdkCameraFocusListener
    {
        void onFocusStart(final TuSdkCameraFocus p0);
        
        void onAutoFocus(final boolean p0, final TuSdkCameraFocus p1);
    }
}
