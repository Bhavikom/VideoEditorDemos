package org.lasque.tusdk.core.utils.hardware;

import android.graphics.RectF;
import java.util.List;
import org.lasque.tusdk.core.components.camera.TuVideoFocusTouchViewBase.GestureListener;
import org.lasque.tusdk.core.media.camera.TuSdkCamera;
import org.lasque.tusdk.core.media.camera.TuSdkCamera.TuSdkCameraStatus;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesVideoCameraInterface;
import org.lasque.tusdk.core.struct.TuSdkSize;

public abstract interface TuSDKVideoCameraFocusViewInterface
{
  public abstract void viewWillDestory();
  
  public abstract void setCamera(SelesVideoCameraInterface paramSelesVideoCameraInterface);
  
  public abstract void setDisableFocusBeep(boolean paramBoolean);
  
  public abstract void setDisableContinueFoucs(boolean paramBoolean);
  
  public abstract void setRegionPercent(RectF paramRectF);
  
  public abstract void setGuideLineViewState(boolean paramBoolean);
  
  public abstract void setEnableFilterConfig(boolean paramBoolean);
  
  public abstract void cameraStateChanged(SelesVideoCameraInterface paramSelesVideoCameraInterface, TuSdkStillCameraAdapter.CameraState paramCameraState);
  
  public abstract void cameraStateChanged(boolean paramBoolean, TuSdkCamera paramTuSdkCamera, TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus);
  
  public abstract void notifyFilterConfigView(SelesOutInput paramSelesOutInput);
  
  public abstract void showRangeView();
  
  public abstract void setRangeViewFoucsState(boolean paramBoolean);
  
  public abstract void setCameraFaceDetection(List<TuSdkFace> paramList, TuSdkSize paramTuSdkSize);
  
  public abstract void setEnableFaceFeatureDetection(boolean paramBoolean);
  
  public abstract void setGestureListener(TuVideoFocusTouchViewBase.GestureListener paramGestureListener);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\core\utils\hardware\TuSDKVideoCameraFocusViewInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */