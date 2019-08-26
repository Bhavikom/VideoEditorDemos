package org.lasque.tusdk.core.utils.hardware;

import android.graphics.RectF;
import java.util.List;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.struct.TuSdkSize;

public abstract interface TuSdkVideoCameraExtendViewInterface
{
  public abstract void viewWillDestory();
  
  public abstract void setCamera(TuSdkStillCameraInterface paramTuSdkStillCameraInterface);
  
  public abstract void setEnableLongTouchCapture(boolean paramBoolean);
  
  public abstract void setDisableFocusBeep(boolean paramBoolean);
  
  public abstract void setDisableContinueFoucs(boolean paramBoolean);
  
  public abstract void setRegionPercent(RectF paramRectF);
  
  public abstract void setGuideLineViewState(boolean paramBoolean);
  
  public abstract void setEnableFilterConfig(boolean paramBoolean);
  
  public abstract void cameraStateChanged(TuSdkStillCameraInterface paramTuSdkStillCameraInterface, TuSdkStillCameraAdapter.CameraState paramCameraState);
  
  public abstract void notifyFilterConfigView(SelesOutInput paramSelesOutInput);
  
  public abstract void showRangeView();
  
  public abstract void setRangeViewFoucsState(boolean paramBoolean);
  
  public abstract void setCameraFaceDetection(List<TuSdkFace> paramList, TuSdkSize paramTuSdkSize);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\hardware\TuSdkVideoCameraExtendViewInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */