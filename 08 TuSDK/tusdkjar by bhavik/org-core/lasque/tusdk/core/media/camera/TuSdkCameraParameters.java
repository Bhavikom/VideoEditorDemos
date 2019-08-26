package org.lasque.tusdk.core.media.camera;

import org.lasque.tusdk.core.utils.hardware.CameraConfigs.CameraFlash;

public abstract interface TuSdkCameraParameters
{
  public abstract void configure(TuSdkCameraBuilder paramTuSdkCameraBuilder);
  
  public abstract void changeStatus(TuSdkCamera.TuSdkCameraStatus paramTuSdkCameraStatus);
  
  public abstract void setFlashMode(CameraConfigs.CameraFlash paramCameraFlash);
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\camera\TuSdkCameraParameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */