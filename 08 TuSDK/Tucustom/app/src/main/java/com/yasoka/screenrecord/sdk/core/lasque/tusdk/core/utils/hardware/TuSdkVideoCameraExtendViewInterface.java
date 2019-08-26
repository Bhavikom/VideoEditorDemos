// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.List;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;

public interface TuSdkVideoCameraExtendViewInterface
{
    void viewWillDestory();
    
    void setCamera(final TuSdkStillCameraInterface p0);
    
    void setEnableLongTouchCapture(final boolean p0);
    
    void setDisableFocusBeep(final boolean p0);
    
    void setDisableContinueFoucs(final boolean p0);
    
    void setRegionPercent(final RectF p0);
    
    void setGuideLineViewState(final boolean p0);
    
    void setEnableFilterConfig(final boolean p0);
    
    void cameraStateChanged(final TuSdkStillCameraInterface p0, final TuSdkStillCameraAdapter.CameraState p1);
    
    void notifyFilterConfigView(final SelesOutInput p0);
    
    void showRangeView();
    
    void setRangeViewFoucsState(final boolean p0);
    
    void setCameraFaceDetection(final List<TuSdkFace> p0, final TuSdkSize p1);
}
