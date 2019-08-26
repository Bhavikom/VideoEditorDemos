// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.components.camera.TuVideoFocusTouchViewBase;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.List;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.media.camera.TuSdkCamera;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCamera;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkFace;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.components.camera.TuVideoFocusTouchViewBase;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources.SelesVideoCameraInterface;
//import org.lasque.tusdk.core.seles.sources.SelesVideoCameraInterface;

public interface TuSDKVideoCameraFocusViewInterface
{
    void viewWillDestory();
    
    void setCamera(final SelesVideoCameraInterface p0);
    
    void setDisableFocusBeep(final boolean p0);
    
    void setDisableContinueFoucs(final boolean p0);
    
    void setRegionPercent(final RectF p0);
    
    void setGuideLineViewState(final boolean p0);
    
    void setEnableFilterConfig(final boolean p0);
    
    void cameraStateChanged(final SelesVideoCameraInterface p0, final TuSdkStillCameraAdapter.CameraState p1);
    
    void cameraStateChanged(final boolean p0, final TuSdkCamera p1, final TuSdkCamera.TuSdkCameraStatus p2);
    
    void notifyFilterConfigView(final SelesOutInput p0);
    
    void showRangeView();
    
    void setRangeViewFoucsState(final boolean p0);
    
    void setCameraFaceDetection(final List<TuSdkFace> p0, final TuSdkSize p1);
    
    void setEnableFaceFeatureDetection(final boolean p0);
    
    void setGestureListener(final TuVideoFocusTouchViewBase.GestureListener p0);
}
