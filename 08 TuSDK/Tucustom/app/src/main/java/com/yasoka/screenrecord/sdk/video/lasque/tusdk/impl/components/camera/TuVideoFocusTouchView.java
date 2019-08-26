// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.impl.components.camera;

import android.view.ViewGroup;
import android.view.LayoutInflater;
//import org.lasque.tusdk.core.media.camera.TuSdkCamera;
//import org.lasque.tusdk.core.seles.sources.SelesVideoCameraInterface;
//import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter;
import android.graphics.PointF;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
//import org.lasque.tusdk.core.utils.hardware.TuSdkFace;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.core.TuSdkContext;
import java.util.ArrayList;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.camera.TuSdkCamera;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkFace;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.components.camera.TuFocusRangeView;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.components.camera.TuVideoFocusTouchViewBase;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.sources.SelesVideoCameraInterface;

import java.util.List;
//import org.lasque.tusdk.modules.components.camera.TuFocusRangeView;
//import org.lasque.tusdk.core.components.camera.TuVideoFocusTouchViewBase;

public class TuVideoFocusTouchView extends TuVideoFocusTouchViewBase
{
    private TuFocusRangeView a;
    private final List<View> b;
    private int c;
    
    public TuVideoFocusTouchView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.b = new ArrayList<View>();
    }
    
    public TuVideoFocusTouchView(final Context context, final AttributeSet set) {
        super(context, set);
        this.b = new ArrayList<View>();
    }
    
    public TuVideoFocusTouchView(final Context context) {
        super(context);
        this.b = new ArrayList<View>();
        this.showViewIn((View)this.getFocusRangeView(), false);
    }
    
    public int getFaceDetectionLayoutID() {
        if (this.c < 1) {
            this.c = TuSdkContext.getLayoutResId("tusdk_impl_component_camera_face_detection_view");
        }
        return this.c;
    }
    
    public View buildFaceDetectionView() {
        return null;
    }
    
    public void onFaceAligmented(final FaceAligment[] array, final TuSdkSize tuSdkSize, final boolean b, final boolean b2) {
        this.hiddenFaceViews();
        if (array == null || array.length <= 0 || tuSdkSize == null) {
            return;
        }
        TuSdkSize create;
        if (b) {
            create = TuSdkSize.create(tuSdkSize.height, tuSdkSize.width);
        }
        else {
            create = tuSdkSize;
        }
        for (int i = 0; i < array.length; ++i) {
            final FaceAligment faceAligment = array[i];
            if (faceAligment.getMarks() != null) {
                if (faceAligment.rect != null) {
                    final ArrayList<TuSdkFace> list = new ArrayList<TuSdkFace>();
                    final TuSdkFace tuSdkFace = new TuSdkFace();
                    tuSdkFace.rect = faceAligment.rect;
                    list.add(tuSdkFace);
                    if (i == 0 && b2) {
                        super.setCameraFaceDetection(list, create);
                    }
                }
            }
        }
    }
    
    public void setGuideLineViewState(final boolean b) {
    }
    
    public void setEnableFilterConfig(final boolean b) {
    }
    
    public void notifyFilterConfigView(final SelesOutInput selesOutInput) {
    }
    
    public void showRangeView() {
    }
    
    @Override
    public void setRangeViewFoucsState(final boolean b) {
        if (this.getFocusRangeView() != null) {
            this.getFocusRangeView().setFoucsState(true);
        }
    }
    
    @Override
    public void showFocusView(final PointF position) {
        if (this.getCamera() == null || !this.getCamera().canSupportAutoFocus() || this.getCamera().getState() != TuSdkStillCameraAdapter.CameraState.StateStarted) {
            return;
        }
        if (this.getFocusRangeView() != null) {
            this.getFocusRangeView().setPosition(position);
        }
    }
    
    @Override
    public void cameraStateChanged(final SelesVideoCameraInterface selesVideoCameraInterface, final TuSdkStillCameraAdapter.CameraState cameraState) {
        super.cameraStateChanged(selesVideoCameraInterface, cameraState);
        if (cameraState == TuSdkStillCameraAdapter.CameraState.StateStarted) {
            this.showViewIn((View)this.getFocusRangeView(), false);
        }
    }
    
    public void cameraStateChanged(final boolean b, final TuSdkCamera tuSdkCamera, final TuSdkCamera.TuSdkCameraStatus tuSdkCameraStatus) {
    }
    
    public TuFocusRangeView getFocusRangeView() {
        if (this.a == null) {
            this.addView((View)(this.a = (TuFocusRangeView)LayoutInflater.from(this.getContext()).inflate(TuSdkContext.getLayoutResId("tusdk_impl_component_camera_focus_range_view"), (ViewGroup)null)), TuSdkContext.dip2px(90.0f), TuSdkContext.dip2px(90.0f));
        }
        return this.a;
    }
}
