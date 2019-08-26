// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.TuSdkResult;
//import org.lasque.tusdk.core.seles.sources.SelesStillCameraInterface;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkResult;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesStillCameraInterface;

public interface TuSdkStillCameraInterface extends SelesStillCameraInterface
{
    void setCameraListener(final TuSdkStillCameraListener p0);
    
    TuSdkStillCameraAdapter<?> adapter();
    
    TuSdkStillCameraAdapter.CameraState getState();
    
    void switchFilter(final String p0);
    
    void setPreviewRatio(final float p0);
    
    void setOutputPictureRatio(final float p0);
    
    public interface TuSdkStillCameraListener
    {
        void onStillCameraStateChanged(final TuSdkStillCameraInterface p0, final TuSdkStillCameraAdapter.CameraState p1);
        
        void onStillCameraTakedPicture(final TuSdkStillCameraInterface p0, final TuSdkResult p1);
        
        void onFilterChanged(final SelesOutInput p0);
    }
}
