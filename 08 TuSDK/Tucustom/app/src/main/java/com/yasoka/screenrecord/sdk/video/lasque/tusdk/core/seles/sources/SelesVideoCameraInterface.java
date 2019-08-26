// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources;

//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesBaseCameraInterface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter;

public interface SelesVideoCameraInterface extends SelesBaseCameraInterface
{
    TuSdkStillCameraAdapter.CameraState getState();
    
    void setRendererFPS(final int p0);
    
    void switchFilter(final String p0);
    
    int getDeviceAngle();
    
    void updateFaceFeatures(final FaceAligment[] p0, final float p1);
}
