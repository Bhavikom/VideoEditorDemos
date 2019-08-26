package org.lasque.tusdk.core.utils.hardware;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesStillCameraInterface;

public abstract interface TuSdkStillCameraInterface
  extends SelesStillCameraInterface
{
  public abstract void setCameraListener(TuSdkStillCameraListener paramTuSdkStillCameraListener);
  
  public abstract TuSdkStillCameraAdapter<?> adapter();
  
  public abstract TuSdkStillCameraAdapter.CameraState getState();
  
  public abstract void switchFilter(String paramString);
  
  public abstract void setPreviewRatio(float paramFloat);
  
  public abstract void setOutputPictureRatio(float paramFloat);
  
  public static abstract interface TuSdkStillCameraListener
  {
    public abstract void onStillCameraStateChanged(TuSdkStillCameraInterface paramTuSdkStillCameraInterface, TuSdkStillCameraAdapter.CameraState paramCameraState);
    
    public abstract void onStillCameraTakedPicture(TuSdkStillCameraInterface paramTuSdkStillCameraInterface, TuSdkResult paramTuSdkResult);
    
    public abstract void onFilterChanged(SelesOutInput paramSelesOutInput);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\hardware\TuSdkStillCameraInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */