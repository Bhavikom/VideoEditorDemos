package org.lasque.tusdk.impl.components.camera;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.components.camera.TuVideoFocusTouchViewBase;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.media.camera.TuSdkCamera;
import org.lasque.tusdk.core.media.camera.TuSdkCamera.TuSdkCameraStatus;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesVideoCameraInterface;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.hardware.TuSdkFace;
import org.lasque.tusdk.core.utils.hardware.TuSdkStillCameraAdapter.CameraState;
import org.lasque.tusdk.modules.components.camera.TuFocusRangeView;

public class TuVideoFocusTouchView
  extends TuVideoFocusTouchViewBase
{
  private TuFocusRangeView a;
  private final List<View> b = new ArrayList();
  private int c;
  
  public TuVideoFocusTouchView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TuVideoFocusTouchView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TuVideoFocusTouchView(Context paramContext)
  {
    super(paramContext);
    showViewIn(getFocusRangeView(), false);
  }
  
  public int getFaceDetectionLayoutID()
  {
    if (this.c < 1) {
      this.c = TuSdkContext.getLayoutResId("tusdk_impl_component_camera_face_detection_view");
    }
    return this.c;
  }
  
  public View buildFaceDetectionView()
  {
    return null;
  }
  
  public void onFaceAligmented(FaceAligment[] paramArrayOfFaceAligment, TuSdkSize paramTuSdkSize, boolean paramBoolean1, boolean paramBoolean2)
  {
    hiddenFaceViews();
    if ((paramArrayOfFaceAligment == null) || (paramArrayOfFaceAligment.length <= 0) || (paramTuSdkSize == null)) {
      return;
    }
    TuSdkSize localTuSdkSize;
    if (paramBoolean1) {
      localTuSdkSize = TuSdkSize.create(paramTuSdkSize.height, paramTuSdkSize.width);
    } else {
      localTuSdkSize = paramTuSdkSize;
    }
    int i = 0;
    int j = paramArrayOfFaceAligment.length;
    while (i < j)
    {
      FaceAligment localFaceAligment = paramArrayOfFaceAligment[i];
      if ((localFaceAligment.getMarks() != null) && (localFaceAligment.rect != null))
      {
        ArrayList localArrayList = new ArrayList();
        TuSdkFace localTuSdkFace = new TuSdkFace();
        localTuSdkFace.rect = localFaceAligment.rect;
        localArrayList.add(localTuSdkFace);
        if ((i == 0) && (paramBoolean2)) {
          super.setCameraFaceDetection(localArrayList, localTuSdkSize);
        }
      }
      i++;
    }
  }
  
  public void setGuideLineViewState(boolean paramBoolean) {}
  
  public void setEnableFilterConfig(boolean paramBoolean) {}
  
  public void notifyFilterConfigView(SelesOutInput paramSelesOutInput) {}
  
  public void showRangeView() {}
  
  public void setRangeViewFoucsState(boolean paramBoolean)
  {
    if (getFocusRangeView() != null) {
      getFocusRangeView().setFoucsState(true);
    }
  }
  
  public void showFocusView(PointF paramPointF)
  {
    if ((getCamera() == null) || (!getCamera().canSupportAutoFocus()) || (getCamera().getState() != TuSdkStillCameraAdapter.CameraState.StateStarted)) {
      return;
    }
    if (getFocusRangeView() != null) {
      getFocusRangeView().setPosition(paramPointF);
    }
  }
  
  public void cameraStateChanged(SelesVideoCameraInterface paramSelesVideoCameraInterface, TuSdkStillCameraAdapter.CameraState paramCameraState)
  {
    super.cameraStateChanged(paramSelesVideoCameraInterface, paramCameraState);
    if (paramCameraState == TuSdkStillCameraAdapter.CameraState.StateStarted) {
      showViewIn(getFocusRangeView(), false);
    }
  }
  
  public void cameraStateChanged(boolean paramBoolean, TuSdkCamera paramTuSdkCamera, TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus) {}
  
  public TuFocusRangeView getFocusRangeView()
  {
    if (this.a == null)
    {
      int i = TuSdkContext.getLayoutResId("tusdk_impl_component_camera_focus_range_view");
      this.a = ((TuFocusRangeView)LayoutInflater.from(getContext()).inflate(i, null));
      addView(this.a, TuSdkContext.dip2px(90.0F), TuSdkContext.dip2px(90.0F));
    }
    return this.a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\impl\components\camera\TuVideoFocusTouchView.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */