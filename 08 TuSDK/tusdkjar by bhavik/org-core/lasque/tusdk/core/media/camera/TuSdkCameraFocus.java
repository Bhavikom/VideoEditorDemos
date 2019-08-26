package org.lasque.tusdk.core.media.camera;

import java.util.List;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.hardware.TuSdkFace;

public abstract interface TuSdkCameraFocus
{
  public abstract void configure(TuSdkCameraBuilder paramTuSdkCameraBuilder, TuSdkCameraOrientation paramTuSdkCameraOrientation, TuSdkCameraSize paramTuSdkCameraSize);
  
  public abstract void changeStatus(TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus);
  
  public abstract boolean allowFocusToShot();
  
  public abstract void autoFocus(TuSdkCameraFocusListener paramTuSdkCameraFocusListener);
  
  public static abstract interface TuSdkCameraFocusFaceListener
  {
    public abstract void onFocusFaceDetection(List<TuSdkFace> paramList, TuSdkSize paramTuSdkSize);
  }
  
  public static abstract interface TuSdkCameraFocusListener
  {
    public abstract void onFocusStart(TuSdkCameraFocus paramTuSdkCameraFocus);
    
    public abstract void onAutoFocus(boolean paramBoolean, TuSdkCameraFocus paramTuSdkCameraFocus);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCameraFocus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */